#!/bin/bash

# Define the stamp exactly as you want it to appear
STAMP=$(cat << 'EOF'
/*
 * Copyright (c) 2026 Aritra Banerjee. All Rights Reserved.
 * GitHub: https://github.com/programmer1128
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 */
EOF
)

# Find all relevant source files, ignoring hidden directories like .git
find . -type f \( -name "*.java" -o -name "*.cpp" -o -name "*.c" -o -name "*.js" \) -not -path "*/\.*" | while read file; do
  # Check if the file already has the stamp to avoid duplicates
  if ! grep -q "Copyright (c) 2026 Aritra Banerjee" "$file"; then
    # Prepend the stamp to the file
    echo "$STAMP" > temp_file
    echo "" >> temp_file
    cat "$file" >> temp_file
    mv temp_file "$file"
    echo "Stamped: $file"
  else
    echo "Already stamped: $file"
  fi
done

echo "Stamping complete!"