#!/bin/bash

# Quick Setup Script for React Frontend
# Run this from the ERP directory (parent of erp-product-be)

echo "🚀 PrintFlow ERP - React Frontend Setup"
echo "========================================"
echo ""

# Check if we're in the right directory
if [ ! -d "erp-product-be" ]; then
    echo "❌ Error: erp-product-be directory not found!"
    echo "Please run this script from the ERP parent directory"
    echo "Example: cd /Users/b0k03wc/Documents/3-8/ERP && bash erp-product-be/setup-frontend.sh"
    exit 1
fi

echo "✅ Found backend directory"
echo ""

# Option 1: Create React App
echo "Option 1: Create React App (Recommended)"
echo "----------------------------------------"
echo "npx create-react-app erp-product-fe"
echo ""
echo "Option 2: Vite (Faster)"
echo "----------------------"
echo "npm create vite@latest erp-product-fe -- --template react"
echo ""

read -p "Choose option (1 or 2): " option

if [ "$option" = "1" ]; then
    echo "📦 Creating React app..."
    npx create-react-app erp-product-fe
elif [ "$option" = "2" ]; then
    echo "⚡ Creating Vite React app..."
    npm create vite@latest erp-product-fe -- --template react
    cd erp-product-fe
    npm install
    cd ..
else
    echo "❌ Invalid option. Exiting."
    exit 1
fi

echo ""
echo "✅ Frontend project created!"
echo ""

# Navigate to frontend directory
cd erp-product-fe

echo "📦 Installing dependencies..."
npm install axios react-router-dom

echo ""
read -p "Install Material-UI for design? (y/n): " install_mui

if [ "$install_mui" = "y" ]; then
    echo "📦 Installing Material-UI..."
    npm install @mui/material @mui/icons-material @emotion/react @emotion/styled
    echo "✅ Material-UI installed!"
fi

echo ""
echo "📁 Creating project structure..."

# Create directories
mkdir -p src/api
mkdir -p src/components/common
mkdir -p src/components/dashboard
mkdir -p src/components/auth
mkdir -p src/pages
mkdir -p src/context
mkdir -p src/utils

echo "✅ Project structure created!"
echo ""

# Create .env file
echo "📝 Creating .env file..."
cat > .env << 'EOF'
REACT_APP_API_BASE_URL=http://localhost:8080
REACT_APP_API_TIMEOUT=30000
EOF

echo "✅ .env file created!"
echo ""

echo "🎉 Setup Complete!"
echo ""
echo "📋 Next Steps:"
echo "=============="
echo ""
echo "1. Make sure backend is running:"
echo "   cd ../erp-product-be"
echo "   mvn spring-boot:run"
echo ""
echo "2. Start frontend development server:"
echo "   npm start                  (for Create React App)"
echo "   npm run dev                (for Vite)"
echo ""
echo "3. Open browser:"
echo "   Frontend: http://localhost:3000 (or http://localhost:5173 for Vite)"
echo "   Backend:  http://localhost:8080"
echo "   Swagger:  http://localhost:8080/swagger-ui.html"
echo ""
echo "4. Test login credentials:"
echo "   username: admin"
echo "   password: admin123"
echo ""
echo "📚 Documentation:"
echo "   - Frontend Guide:     ../erp-product-be/frontend-docs/REACT_FRONTEND_GUIDE.md"
echo "   - API Reference:      ../erp-product-be/frontend-docs/FRONTEND_API_REFERENCE.md"
echo "   - Frontend Summary:   ../erp-product-be/frontend-docs/FRONTEND_COMPLETE_GUIDE.md"
echo "   - Design System:      ../erp-product-be/frontend-docs/DESIGN_SYSTEM.md"
echo "   - Swagger Docs:       ../erp-product-be/SWAGGER_DOCUMENTATION.md"
echo ""
echo "💡 Need starter code files? Just ask and I'll create:"
echo "   - axios.js, authApi.js, dashboardApi.js"
echo "   - LoginPage.jsx, DashboardPage.jsx"
echo "   - AuthContext.jsx, App.jsx"
echo ""
echo "Happy coding! 🚀"
