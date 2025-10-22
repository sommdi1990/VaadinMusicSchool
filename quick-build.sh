#!/bin/bash

# Quick Build Script for Music School Management System
# Fast build without tests for development

set -e

# Set Java 21
export JAVA_HOME="/usr/lib/jvm/java-21-openjdk-amd64"
export PATH="$JAVA_HOME/bin:$PATH"

echo "🚀 Quick Build - Music School Management System"
echo "Java Version: $(java -version 2>&1 | head -n 1)"
echo ""

# Clean and package
echo "📦 Building application..."
mvn clean package -DskipTests=true

echo ""
echo "✅ Build completed!"
echo "📁 JAR file: target/music-school-management-1.0.0.jar"
echo ""
echo "To run: java -jar target/music-school-management-1.0.0.jar"
echo "To run with Maven: mvn spring-boot:run"
