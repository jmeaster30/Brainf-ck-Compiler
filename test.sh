if [[ -n "$1" ]]; then
	echo "Running tests from $1"
	if [[ "$1" == "java" ]]; then
		cd $1
		TEST=(../tests/$2)
		javac BFC.java
	  echo "****** Executing test $2 ******"
		java BFC -c $TEST
		echo ""
		cd ..
	else
		echo "that folder does not exist"
	fi
	./clean.sh $1
else
	echo "Specify which folder to run the tests from"
fi
