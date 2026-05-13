// Material-UI Theme Configuration
// Copy this file to: src/theme.js

import { createTheme } from '@mui/material/styles';

const theme = createTheme({
  // Color Palette
  palette: {
    mode: 'light',
    primary: {
      main: '#1275e2',       // Primary Blue
      light: '#4a95ea',
      dark: '#0f5fc1',
      contrastText: '#ffffff',
    },
    secondary: {
      main: '#5f78a3',       // Secondary Slate Blue
      light: '#7f94b8',
      dark: '#4a5d7f',
      contrastText: '#ffffff',
    },
    tertiary: {
      main: '#c55b00',       // Tertiary Amber
      light: '#d47b2f',
      dark: '#a04800',
      contrastText: '#ffffff',
    },
    success: {
      main: '#10b981',
      light: '#34d399',
      dark: '#059669',
    },
    error: {
      main: '#ef4444',
      light: '#f87171',
      dark: '#dc2626',
    },
    warning: {
      main: '#f59e0b',
      light: '#fbbf24',
      dark: '#d97706',
    },
    info: {
      main: '#3b82f6',
      light: '#60a5fa',
      dark: '#2563eb',
    },
    grey: {
      50: '#f9fafb',
      100: '#f3f4f6',
      200: '#e5e7eb',
      300: '#d1d5db',
      400: '#9ca3af',
      500: '#6b7280',
      600: '#4b5563',
      700: '#374151',
      800: '#1f2937',
      900: '#111827',
    },
    neutral: {
      main: '#74777f',       // Neutral Grey
    },
    background: {
      default: '#f9fafb',
      paper: '#ffffff',
    },
    text: {
      primary: '#111827',
      secondary: '#6b7280',
      disabled: '#9ca3af',
    },
    divider: '#e5e7eb',
  },

  // Typography
  typography: {
    fontFamily: "'Inter', -apple-system, BlinkMacSystemFont, 'Segoe UI', sans-serif",
    h1: {
      fontSize: '2rem',        // 32px
      fontWeight: 700,
      lineHeight: 1.25,
      color: '#111827',
    },
    h2: {
      fontSize: '1.5rem',      // 24px
      fontWeight: 600,
      lineHeight: 1.25,
      color: '#111827',
    },
    h3: {
      fontSize: '1.25rem',     // 20px
      fontWeight: 600,
      lineHeight: 1.25,
      color: '#111827',
    },
    h4: {
      fontSize: '1.125rem',    // 18px
      fontWeight: 600,
      lineHeight: 1.25,
      color: '#111827',
    },
    h5: {
      fontSize: '1rem',        // 16px
      fontWeight: 600,
      lineHeight: 1.25,
      color: '#111827',
    },
    h6: {
      fontSize: '0.875rem',    // 14px
      fontWeight: 600,
      lineHeight: 1.25,
      color: '#111827',
    },
    body1: {
      fontSize: '1rem',        // 16px
      fontWeight: 400,
      lineHeight: 1.5,
      color: '#1f2937',
    },
    body2: {
      fontSize: '0.875rem',    // 14px
      fontWeight: 400,
      lineHeight: 1.5,
      color: '#1f2937',
    },
    caption: {
      fontSize: '0.75rem',     // 12px
      fontWeight: 400,
      lineHeight: 1.5,
      color: '#6b7280',
    },
    button: {
      fontSize: '0.875rem',    // 14px
      fontWeight: 600,
      textTransform: 'none',
      letterSpacing: 0,
    },
  },

  // Spacing (8px baseline)
  spacing: 4,  // 1 unit = 4px

  // Shape (Border Radius)
  shape: {
    borderRadius: 8,  // Level 2: Moderate roundedness
  },

  // Component Overrides
  components: {
    MuiButton: {
      styleOverrides: {
        root: {
          borderRadius: 8,
          padding: '12px 24px',
          fontSize: '14px',
          fontWeight: 600,
          textTransform: 'none',
          boxShadow: 'none',
          '&:hover': {
            boxShadow: 'none',
          },
        },
        contained: {
          '&:hover': {
            boxShadow: '0 2px 4px rgba(0, 0, 0, 0.1)',
          },
        },
        outlined: {
          borderWidth: '1px',
          '&:hover': {
            borderWidth: '1px',
          },
        },
      },
    },
    MuiCard: {
      styleOverrides: {
        root: {
          borderRadius: 12,
          border: '1px solid #e5e7eb',
          boxShadow: '0 1px 3px rgba(0, 0, 0, 0.1)',
          '&:hover': {
            boxShadow: '0 4px 6px rgba(0, 0, 0, 0.1)',
          },
        },
      },
    },
    MuiCardContent: {
      styleOverrides: {
        root: {
          padding: 24,
          '&:last-child': {
            paddingBottom: 24,
          },
        },
      },
    },
    MuiTextField: {
      styleOverrides: {
        root: {
          '& .MuiOutlinedInput-root': {
            borderRadius: 8,
            '& fieldset': {
              borderColor: '#d1d5db',
            },
            '&:hover fieldset': {
              borderColor: '#9ca3af',
            },
            '&.Mui-focused fieldset': {
              borderColor: '#1275e2',
              borderWidth: '1px',
            },
          },
        },
      },
    },
    MuiOutlinedInput: {
      styleOverrides: {
        root: {
          borderRadius: 8,
          fontSize: '14px',
        },
        input: {
          padding: '10px 14px',
        },
      },
    },
    MuiChip: {
      styleOverrides: {
        root: {
          borderRadius: 4,
          fontSize: '12px',
          fontWeight: 600,
          height: 'auto',
          padding: '4px 8px',
        },
      },
    },
    MuiAlert: {
      styleOverrides: {
        root: {
          borderRadius: 8,
          padding: '16px',
          borderLeftWidth: 4,
          borderLeftStyle: 'solid',
        },
        standardSuccess: {
          backgroundColor: '#d1fae5',
          color: '#065f46',
          borderLeftColor: '#10b981',
        },
        standardError: {
          backgroundColor: '#fee2e2',
          color: '#991b1b',
          borderLeftColor: '#ef4444',
        },
        standardWarning: {
          backgroundColor: '#fef3c7',
          color: '#92400e',
          borderLeftColor: '#f59e0b',
        },
        standardInfo: {
          backgroundColor: '#dbeafe',
          color: '#1e40af',
          borderLeftColor: '#3b82f6',
        },
      },
    },
    MuiTableHead: {
      styleOverrides: {
        root: {
          backgroundColor: '#f9fafb',
          '& .MuiTableCell-head': {
            color: '#374151',
            fontWeight: 600,
            fontSize: '12px',
            textTransform: 'uppercase',
            letterSpacing: '0.05em',
            padding: '12px 16px',
          },
        },
      },
    },
    MuiTableBody: {
      styleOverrides: {
        root: {
          '& .MuiTableRow-root': {
            '&:hover': {
              backgroundColor: '#f9fafb',
            },
          },
          '& .MuiTableCell-root': {
            borderBottom: '1px solid #f3f4f6',
            padding: '12px 16px',
            fontSize: '14px',
            color: '#1f2937',
          },
        },
      },
    },
    MuiAppBar: {
      styleOverrides: {
        root: {
          backgroundColor: '#ffffff',
          color: '#111827',
          boxShadow: '0 1px 3px rgba(0, 0, 0, 0.1)',
        },
      },
    },
    MuiDrawer: {
      styleOverrides: {
        paper: {
          backgroundColor: '#ffffff',
          borderRight: '1px solid #e5e7eb',
        },
      },
    },
  },

  // Breakpoints
  breakpoints: {
    values: {
      xs: 0,
      sm: 640,
      md: 768,
      lg: 1024,
      xl: 1280,
    },
  },
});

export default theme;
