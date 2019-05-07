if [[ -n "$1" ]]; then
	echo "Running tests from $1"
	if [[ "$1" == "java" ]]; then
		cd $1
		TESTS=(../tests/*)
		javac BFC.java
		for f in "${TESTS[@]}"; do
			echo "****** Executing test $f ******"
			java BFC -c -i $f
			echo ""
		done
		cd ..
	else
		echo "that folder does not exist"
	fi
	./clean.sh $1
else
	echo "Specify which folder to run the tests from"
fi