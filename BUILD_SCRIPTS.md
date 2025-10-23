# 🛠️ Build Scripts Documentation

## Overview

This document describes the build scripts available for the Music School Management System. These scripts automate the build process and ensure consistent deployment across different environments.

## Available Scripts

### 1. `build.sh` - Complete Build Process

**Purpose**: Full build process with tests, Docker preparation, and comprehensive validation.

**Usage**:
```bash
./build.sh
```

**Features**:
- ✅ **Java 21 Verification**: Automatically checks and sets Java 21
- ✅ **Clean Build**: Removes previous build artifacts
- ✅ **Test Execution**: Runs all unit tests
- ✅ **Package Creation**: Creates executable JAR file
- ✅ **Docker Preparation**: Ensures Docker files are ready
- ✅ **Colored Output**: Beautiful colored terminal output
- ✅ **Error Handling**: Comprehensive error checking and reporting

**Output**:
- JAR file: `target/music-school-management-1.0.0.jar`
- Docker files: `Dockerfile`, `docker-compose.yml`
- Build logs with colored status indicators

### 2. `quick-build.sh` - Fast Development Build

**Purpose**: Quick build for development without tests.

**Usage**:
```bash
./quick-build.sh
```

**Features**:
- ⚡ **Fast Build**: Skips tests for faster development cycles
- 🚀 **Development Ready**: Perfect for iterative development
- 📦 **JAR Creation**: Creates deployable JAR file
- 🎯 **Minimal Dependencies**: Only essential build steps

**Output**:
- JAR file: `target/music-school-management-1.0.0.jar`
- Build time: ~30-60 seconds (vs 2-3 minutes for full build)

## Script Features

### Java 21 Management

Both scripts automatically handle Java 21 setup:

```bash
# Automatic Java 21 detection and setup
export JAVA_HOME="/home/soroush/.jdks/graalvm-jdk-21.0.8"
export PATH="$JAVA_HOME/bin:$PATH"

# Version verification
java -version  # Ensures Java 21 is active
```

### Error Handling

- **Exit on Error**: Scripts stop immediately on any error
- **Colored Output**: Clear visual feedback for success/failure
- **Progress Indicators**: Shows current build step
- **Error Messages**: Detailed error reporting

### Docker Integration

The `build.sh` script automatically prepares Docker files:

```bash
# Creates Dockerfile if missing
# Creates docker-compose.yml if missing
# Ensures Docker files are up to date
```

## Usage Examples

### Development Workflow

```bash
# 1. Quick build for development
./quick-build.sh

# 2. Run application
java -jar target/music-school-management-1.0.0.jar

# 3. Or run with Maven
mvn spring-boot:run
```

### Production Workflow

```bash
# 1. Full build with tests
./build.sh

# 2. Deploy with Docker
docker-compose up --build

# 3. Or run JAR directly
java -jar target/music-school-management-1.0.0.jar
```

### CI/CD Integration

```bash
# In CI/CD pipeline
./build.sh

# Check exit code
if [ $? -eq 0 ]; then
    echo "Build successful"
    # Deploy application
else
    echo "Build failed"
    exit 1
fi
```

## Environment Requirements

### Prerequisites

- **Java 21**: OpenJDK 21 or Oracle JDK 21
- **Maven 3.9+**: Apache Maven for build management
- **Docker**: For containerized deployment (optional)

### System Requirements

- **Memory**: Minimum 2GB RAM for build process
- **Disk Space**: 500MB for build artifacts
- **CPU**: Multi-core recommended for faster builds

## Troubleshooting

### Common Issues

#### 1. Java Version Issues
```bash
# Error: Java version is not 21
# Solution: Install Java 21
sudo apt install openjdk-21-jdk
```

#### 2. Maven Issues
```bash
# Error: Maven not found
# Solution: Install Maven
sudo apt install maven
```

#### 3. Permission Issues
```bash
# Error: Permission denied
# Solution: Make scripts executable
chmod +x build.sh quick-build.sh
```

#### 4. Memory Issues
```bash
# Error: Out of memory during build
# Solution: Increase Maven heap size
export MAVEN_OPTS="-Xmx2g -Xms1g"
```

### Debug Mode

Run scripts with debug information:

```bash
# Enable debug output
set -x
./build.sh

# Or run Maven with debug
mvn clean package -X
```

## Customization

### Environment Variables

You can customize the build process with environment variables:

```bash
# Custom Java home
export JAVA_HOME="/custom/path/to/java21"

# Custom Maven options
export MAVEN_OPTS="-Xmx4g -Xms2g"

# Custom build profile
export SPRING_PROFILES_ACTIVE="production"
```

### Script Modification

To modify the build process, edit the scripts:

```bash
# Edit build script
nano build.sh

# Edit quick build script
nano quick-build.sh
```

## Integration with IDEs

### IntelliJ IDEA

1. **External Tools**:
   - Go to `File > Settings > Tools > External Tools`
   - Add new tool with command: `./build.sh`
   - Set working directory to project root

2. **Run Configurations**:
   - Create new Maven run configuration
   - Command line: `clean package`
   - Working directory: project root

### VS Code

1. **Tasks** (`.vscode/tasks.json`):
```json
{
    "version": "2.0.0",
    "tasks": [
        {
            "label": "Build Full",
            "type": "shell",
            "command": "./build.sh",
            "group": "build"
        },
        {
            "label": "Build Quick",
            "type": "shell",
            "command": "./quick-build.sh",
            "group": "build"
        }
    ]
}
```

### Eclipse

1. **External Tools**:
   - Go to `Run > External Tools > External Tools Configurations`
   - Add new program with location: `./build.sh`
   - Set working directory to project root

## Performance Optimization

### Build Time Optimization

```bash
# Use parallel Maven builds
export MAVEN_OPTS="-T 4C"

# Skip tests for faster builds
mvn clean package -DskipTests

# Use local Maven repository
mvn clean package -Dmaven.repo.local=~/.m2/repository
```

### Memory Optimization

```bash
# Optimize JVM settings
export JAVA_OPTS="-Xmx2g -Xms1g -XX:+UseG1GC"

# Optimize Maven settings
export MAVEN_OPTS="-Xmx1g -Xms512m"
```

## Best Practices

### 1. Use Build Scripts
- Always use `./build.sh` for production builds
- Use `./quick-build.sh` for development
- Never run Maven commands directly in production

### 2. Environment Setup
- Set Java 21 before running scripts
- Ensure Maven is properly configured
- Check Docker availability for containerized builds

### 3. Error Handling
- Always check script exit codes
- Review build logs for warnings
- Test builds in clean environments

### 4. Security
- Never commit sensitive information to scripts
- Use environment variables for secrets
- Validate all inputs and dependencies

## Support

For issues with build scripts:

1. **Check Prerequisites**: Ensure Java 21 and Maven are installed
2. **Review Logs**: Check build output for specific errors
3. **Test Environment**: Try building in a clean environment
4. **Update Scripts**: Ensure you have the latest version of build scripts

## Changelog

### Version 1.0.0
- Initial release of build scripts
- Java 21 support
- Docker integration
- Colored output and error handling
- Comprehensive documentation
