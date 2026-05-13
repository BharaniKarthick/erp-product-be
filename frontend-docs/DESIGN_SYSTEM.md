# PrintFlow ERP - Design System

## Design Philosophy
**Precision & Clarity**: A design system built on functional clarity and professional reliability, utilizing high-performance sans-serif typography and balanced spatial relationships for a modern, high-fidelity experience.

---

## 🎨 Color Palette

### Primary Colors

#### Primary Blue - #1275e2
- **Usage**: Primary actions, progress indicators, active states, CTAs
- **RGB**: rgb(18, 117, 226)
- **Use for**: Buttons, links, selected states, brand emphasis

#### Secondary Slate Blue - #5f78a3
- **Usage**: Supporting UI components, secondary navigation
- **RGB**: rgb(95, 120, 163)
- **Use for**: Secondary buttons, sidebar, supporting text

#### Tertiary Amber - #c55b00
- **Usage**: Highlights, alerts, decorative elements requiring attention
- **RGB**: rgb(197, 91, 0)
- **Use for**: Warning badges, important notifications, attention indicators

#### Neutral Grey - #74777f
- **Usage**: Structure, background stability, supporting elements
- **RGB**: rgb(116, 119, 127)
- **Use for**: Borders, dividers, muted text, backgrounds

### Extended Palette

#### Success - #10b981
- For positive actions, success messages, profit indicators

#### Error - #ef4444
- For errors, critical alerts, loss indicators

#### Warning - #f59e0b
- For warnings, pending states

#### Info - #3b82f6
- For informational messages

### Neutrals (Grey Scale)
```
Grey 50:  #f9fafb (Backgrounds)
Grey 100: #f3f4f6 (Light backgrounds)
Grey 200: #e5e7eb (Borders)
Grey 300: #d1d5db (Dividers)
Grey 400: #9ca3af (Disabled text)
Grey 500: #6b7280 (Secondary text)
Grey 600: #4b5563 (Body text)
Grey 700: #374151 (Headings)
Grey 800: #1f2937 (Dark text)
Grey 900: #111827 (Darkest text)
```

---

## 📝 Typography

### Typeface
**Inter** - Applied across all text elements (headlines, body copy, labels)

**Why Inter?**
- Tall x-height for excellent legibility
- Ideal for data-heavy interfaces
- Professional and modern appearance
- Optimized for digital screens

### Font Weights
```
Light:    300 (Subtle labels)
Regular:  400 (Body text)
Medium:   500 (Emphasis)
Semibold: 600 (Headings)
Bold:     700 (Strong emphasis)
```

### Type Scale
```
Heading 1: 32px / 2rem    - font-weight: 700 (Page titles)
Heading 2: 24px / 1.5rem  - font-weight: 600 (Section headers)
Heading 3: 20px / 1.25rem - font-weight: 600 (Subsection headers)
Heading 4: 18px / 1.125rem - font-weight: 600 (Card headers)
Body Large: 16px / 1rem   - font-weight: 400 (Emphasis body)
Body: 14px / 0.875rem     - font-weight: 400 (Default text)
Body Small: 12px / 0.75rem - font-weight: 400 (Supporting text)
Caption: 11px / 0.6875rem - font-weight: 400 (Metadata, timestamps)
```

### Line Heights
```
Tight:  1.25 (Headings)
Normal: 1.5  (Body text)
Relaxed: 1.75 (Long-form content)
```

### Import (Add to index.html or CSS)
```html
<link rel="preconnect" href="https://fonts.googleapis.com">
<link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
<link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700&display=swap" rel="stylesheet">
```

```css
body {
  font-family: 'Inter', -apple-system, BlinkMacSystemFont, 'Segoe UI', sans-serif;
}
```

---

## 📐 Spatial System

### UI Geometry

#### Roundedness: Level 2 (Moderate)
Moderate corner radii providing balance between organic softness and structured precision.

```
Small:  4px  (Badges, tags)
Medium: 8px  (Buttons, inputs, cards)
Large:  12px (Modals, containers)
XLarge: 16px (Major sections)
```

#### Spacing: Level 2 (Standard Rhythm)
Standard spacing providing breathing room without sacrificing information density.

```
xs:  4px   (Tight spacing)
sm:  8px   (Compact spacing)
md:  16px  (Standard spacing)
lg:  24px  (Comfortable spacing)
xl:  32px  (Generous spacing)
2xl: 48px  (Section spacing)
3xl: 64px  (Major section spacing)
```

### Layout Grid
```
Desktop: 12 columns, 24px gutter
Tablet:  8 columns, 16px gutter
Mobile:  4 columns, 16px gutter
```

### Container Max Widths
```
sm:  640px
md:  768px
lg:  1024px
xl:  1280px
2xl: 1536px
```

---

## 🎯 Component Patterns

### Buttons

#### Primary Button
```
Background: #1275e2
Color: #ffffff
Padding: 12px 24px
Border Radius: 8px
Font Weight: 600
Font Size: 14px

Hover: Background #0f5fc1
Active: Background #0d4fa0
Disabled: Background #e5e7eb, Color #9ca3af
```

#### Secondary Button
```
Background: #5f78a3
Color: #ffffff
Padding: 12px 24px
Border Radius: 8px
Font Weight: 600
Font Size: 14px
```

#### Outline Button
```
Background: transparent
Color: #1275e2
Border: 1px solid #1275e2
Padding: 12px 24px
Border Radius: 8px
```

### Cards
```
Background: #ffffff
Border: 1px solid #e5e7eb
Border Radius: 12px
Padding: 24px
Shadow: 0 1px 3px rgba(0, 0, 0, 0.1)

Hover: Shadow: 0 4px 6px rgba(0, 0, 0, 0.1)
```

### Inputs
```
Background: #ffffff
Border: 1px solid #d1d5db
Border Radius: 8px
Padding: 10px 14px
Font Size: 14px
Color: #374151

Focus: Border #1275e2, Shadow 0 0 0 3px rgba(18, 117, 226, 0.1)
Error: Border #ef4444, Shadow 0 0 0 3px rgba(239, 68, 68, 0.1)
```

### Alerts
```
Info:    Background #dbeafe, Border #3b82f6, Color #1e40af
Success: Background #d1fae5, Border #10b981, Color #065f46
Warning: Background #fef3c7, Border #f59e0b, Color #92400e
Error:   Background #fee2e2, Border #ef4444, Color #991b1b

Padding: 16px
Border Radius: 8px
Border Left Width: 4px
```

### Badges
```
Padding: 4px 8px
Border Radius: 4px
Font Size: 12px
Font Weight: 600

Status Colors:
- Active: Background #d1fae5, Color #065f46
- Pending: Background #fef3c7, Color #92400e
- Completed: Background #dbeafe, Color #1e40af
- Critical: Background #fee2e2, Color #991b1b
```

---

## 📊 Dashboard Specific Elements

### KPI Cards
```
Background: #ffffff
Border: 1px solid #e5e7eb
Border Radius: 12px
Padding: 24px
Min Height: 120px

Header:
- Font Size: 14px
- Color: #6b7280
- Font Weight: 500

Value:
- Font Size: 32px
- Color: #111827
- Font Weight: 700

Growth Indicator:
- Positive: Color #10b981, Icon: ↑
- Negative: Color #ef4444, Icon: ↓
```

### Alert Sections
```
Background: #fef3c7 (or appropriate alert color)
Border Left: 4px solid #c55b00
Padding: 16px
Border Radius: 8px
Gap: 12px

Icon Size: 20px
Title: Font size 16px, Font weight 600
Description: Font size 14px, Color #6b7280
```

### Data Tables
```
Header:
- Background: #f9fafb
- Color: #374151
- Font Weight: 600
- Font Size: 12px
- Text Transform: uppercase
- Letter Spacing: 0.05em
- Padding: 12px 16px

Rows:
- Background: #ffffff
- Border Bottom: 1px solid #f3f4f6
- Padding: 12px 16px
- Font Size: 14px
- Color: #1f2937

Hover: Background #f9fafb

Alternating Rows (optional):
- Even: Background #fafafa
```

---

## 🌓 Theme Mode
**Light Mode** - Optimized for daytime readability and high-clarity environments

```
Page Background: #f9fafb
Content Background: #ffffff
Text Primary: #111827
Text Secondary: #6b7280
Text Tertiary: #9ca3af
Border: #e5e7eb
Divider: #d1d5db
```

---

## 🎨 Chart Colors (for Reports)
```
Series 1: #1275e2 (Primary Blue)
Series 2: #5f78a3 (Secondary Slate)
Series 3: #c55b00 (Tertiary Amber)
Series 4: #10b981 (Success Green)
Series 5: #f59e0b (Warning Orange)
Series 6: #ef4444 (Error Red)
Series 7: #8b5cf6 (Purple)
Series 8: #ec4899 (Pink)
```

---

## ♿ Accessibility

### Color Contrast
All text meets WCAG 2.1 Level AA standards:
- Large text (18px+): Minimum 3:1 contrast
- Normal text: Minimum 4.5:1 contrast
- UI components: Minimum 3:1 contrast

### Focus States
All interactive elements have visible focus states:
```
Box Shadow: 0 0 0 3px rgba(18, 117, 226, 0.3)
Outline: none
Border Radius: matches element
```

### Keyboard Navigation
- All actions accessible via keyboard
- Logical tab order
- Skip links for main content

---

## 📱 Responsive Breakpoints
```
Mobile:  < 640px
Tablet:  640px - 1024px
Desktop: > 1024px
```

---

## 🚀 Implementation Guide

### CSS Custom Properties
```css
:root {
  /* Colors */
  --color-primary: #1275e2;
  --color-secondary: #5f78a3;
  --color-tertiary: #c55b00;
  --color-neutral: #74777f;
  
  --color-success: #10b981;
  --color-error: #ef4444;
  --color-warning: #f59e0b;
  --color-info: #3b82f6;
  
  /* Greys */
  --grey-50: #f9fafb;
  --grey-600: #4b5563;
  --grey-900: #111827;
  
  /* Spacing */
  --spacing-xs: 4px;
  --spacing-sm: 8px;
  --spacing-md: 16px;
  --spacing-lg: 24px;
  --spacing-xl: 32px;
  
  /* Border Radius */
  --radius-sm: 4px;
  --radius-md: 8px;
  --radius-lg: 12px;
  --radius-xl: 16px;
  
  /* Typography */
  --font-family: 'Inter', -apple-system, BlinkMacSystemFont, 'Segoe UI', sans-serif;
  --font-size-body: 14px;
  --font-size-h1: 32px;
  --font-size-h2: 24px;
}
```

---

## 📚 Usage Examples

### React Component with Design System
```jsx
// Button.jsx
const Button = ({ variant = 'primary', children, ...props }) => {
  const styles = {
    primary: {
      backgroundColor: '#1275e2',
      color: '#ffffff',
      padding: '12px 24px',
      borderRadius: '8px',
      fontWeight: 600,
      fontSize: '14px',
      border: 'none',
      cursor: 'pointer',
      fontFamily: 'Inter, sans-serif'
    }
  };
  
  return <button style={styles[variant]} {...props}>{children}</button>;
};
```

### With Material-UI Theme
See `theme.js` for complete Material-UI theme configuration.

---

**This design system ensures consistency, accessibility, and professional aesthetics across the entire PrintFlow ERP application.**
