#!/bin/bash

echo "Currently in directory: $(pwd)"
echo "Renaming font files in this directory to lowercase..."

# Loop through all .ttf and .otf files in the current directory
# -maxdepth 1 ensures it only looks in the current directory, not subdirectories
# -type f ensures it only processes files
# -iname is case-insensitive matching for extensions
# -print0 and read -d $'\0' handle filenames with spaces or special characters safely
find . -maxdepth 1 -type f \( -iname "*.ttf" -o -iname "*.otf" \) -print0 | while IFS= read -r -d $'\0' FILE; do
    # Get just the filename from the path (e.g., "./MyFont.ttf" -> "MyFont.ttf")
    FILENAME=$(basename "$FILE")
    # Convert to lowercase
    NEW_FILENAME=$(echo "$FILENAME" | tr '[:upper:]' '[:lower:]')

    if [ "$FILENAME" != "$NEW_FILENAME" ]; then
        # Check for potential conflicts if a lowercase version already exists and is a DIFFERENT file
        # (unlikely if you're just renaming, but good for robustness).
        # 'mv' on case-insensitive filesystems usually handles "MyFont.ttf" -> "myfont.ttf" fine by overwriting.
        # This check is more for case-sensitive filesystems where "MyFont.ttf" and "myfont.ttf" could be distinct.
        if [ -e "$NEW_FILENAME" ] && [ "$(realpath "$FILE")" != "$(realpath "$NEW_FILENAME")" ]; then
             echo "WARNING: Target '$NEW_FILENAME' already exists and is a different file. Skipping '$FILENAME'."
        else
            mv -v "$FILENAME" "$NEW_FILENAME"
            # The -v option in mv already prints what it's doing, e.g., "renamed './MyFont.ttf' -> './myfont.ttf'"
            # echo "Renamed '$FILENAME' to '$NEW_FILENAME'" # This would be redundant with mv -v
        fi
    else
        echo "Filename '$FILENAME' is already lowercase. Skipping."
    fi
done

echo ""
echo "Script finished."
echo "---------------------------------------------------------------------"
echo "IMPORTANT REMINDERS:"
echo "1. Go back to Android Studio."
echo "2. Sync Gradle Files with Project."
echo "3. Clean and Rebuild your project."
echo "4. Manually verify and update any direct code references to the old"
echo "   font names if they were case-sensitive (e.g., in strings or if"
echo "   your resource system was very specific)."
echo "   For typical 'R.font.name' or 'Res.font.name', a rebuild should suffice"
echo "   as long as the base name (before extension) is what's used."
echo "---------------------------------------------------------------------"

