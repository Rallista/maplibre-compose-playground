#!/bin/bash

# Get the last version from build.gradle.kts
last_version=$(grep "version = " build.gradle.kts | sed 's/.*version = "\(.*\)".*/\1/')

echo "Last version: $last_version"

# Determine the next version based on the last tag and the desired version
# bump type in argument 1
if [ "$1" == "major" ]; then
    next_version=$(echo $last_version | awk -F. '{print $1+1".0.0"}')
elif [ "$1" == "minor" ]; then
    next_version=$(echo $last_version | awk -F. '{print $1"."$2+1".0"}')
elif [ "$1" == "patch" ]; then
    next_version=$(echo $last_version | awk -F. '{print $1"."$2"."$3+1}')
else
    echo "Invalid version bump type. Please use 'major', 'minor', or 'patch'."
    exit 1
fi

echo "Bumping version from $last_version to $next_version"

# Update the version in build.gradle.kts
sed -i "s/version = \"$last_version\"/version = \"$next_version\"/" build.gradle.kts

# Output the tag to stdout
echo ::set-output name=version::$next_version
