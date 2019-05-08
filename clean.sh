if [[ -n "$1" ]]; then
	echo "Cleaning up stuff in $1"
	if [[ "$1" == "java" ]]; then
		cd $1
		FILES=$(find . \( -iname "*.class" -o -iname "*.exe" \) -type f)
		if [[ -n "$FILES" ]]; then
			echo "$FILES"
			find . \( -iname "*.class" -o -iname "*.exe" \) -type f -delete
		elif [[ -z "$FILES" ]]; then
			echo "Folder is clean."
		fi
  elif [[ "$1" == "c" ]]; then
    echo "Folder is clean."
	else
		echo "that folder does not exist"
	fi
else
	echo "specify a folder to clean pls"
fi
