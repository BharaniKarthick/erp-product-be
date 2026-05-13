-- Fix order profit trigger to use actual order revenue instead of unit quoted price.
-- This prevents margin_percentage overflow when actual costs exceed the unit price.

CREATE OR REPLACE FUNCTION calculate_order_profit()
RETURNS TRIGGER AS $$
DECLARE
    effective_revenue DECIMAL(15, 2);
    effective_total_cost DECIMAL(15, 2);
    computed_margin DECIMAL(15, 2);
BEGIN
    effective_revenue := COALESCE(
        NULLIF(NEW.total_amount, 0),
        COALESCE(NEW.quoted_price, 0) * COALESCE(NEW.order_quantity, 0),
        0
    );

    effective_total_cost := COALESCE(NEW.actual_labor_cost, 0)
        + COALESCE(NEW.actual_material_cost, 0)
        + COALESCE(NEW.actual_machine_cost, 0);

    NEW.profit_loss := effective_revenue - effective_total_cost;

    IF effective_revenue > 0 THEN
        computed_margin := (NEW.profit_loss / effective_revenue) * 100;
        NEW.margin_percentage := GREATEST(LEAST(computed_margin, 999.99), -999.99);
    ELSE
        NEW.margin_percentage := 0;
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;
