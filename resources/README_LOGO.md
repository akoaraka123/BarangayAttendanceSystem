# Barangay Logo Setup

## How to Add the Logo

1. **Place your logo image** in one of these locations:
   - `resources/barangay_logo.png` (project root)
   - `src/resources/barangay_logo.png` (source resources)

2. **Supported formats**: PNG, JPG, JPEG

3. **Recommended size**: 
   - Square or circular logo works best
   - Minimum: 200x200 pixels
   - Maximum: 1000x1000 pixels (will be scaled automatically)

## Logo Display

The logo will appear as:
- **Gray/transparent background** in the admin dashboard
- **Centered** in the main content area
- **Semi-transparent** (15% opacity) for subtle effect
- **Automatically scaled** to fit the panel size

## File Name

The system looks for: `barangay_logo.png`

If you have a different format (JPG), rename it to PNG or update the code to support your format.

## Example

```
BarangayAttendanceSystem/
├── resources/
│   └── barangay_logo.png  ← Place your logo here
└── src/
    └── resources/
        └── barangay_logo.png  ← Or here
```

