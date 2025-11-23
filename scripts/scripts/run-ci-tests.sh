#!/bin/bash

# Script for CI environment test execution

set -e

echo "Starting CI test execution..."

# Environment variables
export BASE_URL=${BASE_URL:-"https://qa-internship.avito.com"}
export THREAD_COUNT=${THREAD_COUNT:-8}

echo "Environment:"
echo "BASE_URL: $BASE_URL"
echo "THREAD_COUNT: $THREAD_COUNT"

# Run tests with CI profile
mvn clean test -Pci

# Generate reports
mvn allure:report jacoco:report

# Check coverage
mvn jacoco:check

echo "CI test execution completed!"