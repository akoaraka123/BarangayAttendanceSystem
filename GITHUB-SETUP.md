# GitHub Setup Guide

## Before Pushing to GitHub

### Step 1: Clean Up Files

Run the cleanup script to remove build artifacts:

```bash
cleanup-for-github.bat
```

This will delete:
- ✅ `build/` folder (compiled classes)
- ✅ `*.jar` files (compiled JARs)
- ✅ `nbproject/private/` (IDE private configs)
- ✅ `deploy/` folder (if exists)
- ✅ Temporary files

### Step 2: Verify .gitignore

The `.gitignore` file is already configured to ignore:
- Build outputs (`build/`, `*.class`)
- JAR files (`*.jar`)
- IDE files (`nbproject/private/`, `.idea/`, `.vscode/`)
- Temporary files (`deploy/`, `temp_build/`)

### Step 3: Files That Will Be Pushed

✅ **Will be pushed:**
- Source code (`src/`)
- SQL files (`database_setup.sql`, `database_migration_rfid.sql`)
- Build scripts (`build_jar.bat`, `build_fat_jar.bat`, `RUN-FAT-JAR.bat`)
- README files (`README.md`, `README-DEPLOYMENT.md`)
- Configuration files (`manifest.mf`, `build.xml`)
- NetBeans project files (`nbproject/` except `private/`)
- `.gitignore`

❌ **Will NOT be pushed (ignored):**
- `build/` folder
- `*.jar` files
- `nbproject/private/`
- `dist/lib/mysql-connector-j-*.jar` (users download separately)
- `deploy/` folder

### Step 4: Initial Git Setup (if needed)

```bash
git init
git add .
git commit -m "Initial commit: Barangay Attendance System"
git branch -M main
git remote add origin <your-github-repo-url>
git push -u origin main
```

### Step 5: For Contributors

Users who clone the repo need to:

1. **Download MySQL Connector:**
   - Download `mysql-connector-j-9.5.0.jar` from https://dev.mysql.com/downloads/connector/j/
   - Place it in `dist/lib/` folder

2. **Build the project:**
   ```bash
   build_fat_jar.bat
   ```

3. **Run the application:**
   ```bash
   java -jar BarangayAttendanceSystem-FAT.jar
   ```

## File Structure After Cleanup

```
BarangayAttendanceSystem/
├── src/                    ✅ Source code
├── nbproject/              ✅ NetBeans project (except private/)
├── dist/
│   └── lib/                ✅ Empty (users add MySQL connector)
├── .gitignore              ✅ Git ignore rules
├── build_jar.bat           ✅ Build script
├── build_fat_jar.bat       ✅ FAT JAR build script
├── cleanup-for-github.bat   ✅ Cleanup script
├── RUN-FAT-JAR.bat         ✅ Launcher script
├── manifest.mf             ✅ JAR manifest
├── build.xml                ✅ Build configuration
├── database_setup.sql       ✅ Database setup
├── database_migration_rfid.sql ✅ Migration script
├── README.md                ✅ Main README
└── README-DEPLOYMENT.md     ✅ Deployment guide
```

## Notes

- **MySQL Connector**: Not included in repo (users download separately)
- **Build Artifacts**: Not included (users build locally)
- **IDE Configs**: Only public configs included (private/ excluded)

