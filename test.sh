if [[ -n "$1" ]]; then
	echo "Running tests from $1"
	if [[ "$1" == "java" ]]; then
		cd $1
		TEST=(../tests/$3)
		javac BFC.java
	  echo "****** Executing test $3 ******"
		java BFC -t $2 -c $TEST
		echo ""
		cd ..
  elif [[ "$1" == "c" ]]; then
    cd $1
		TEST=(../tests/$3)
		./compile.sh
	  echo "****** Executing test $3 ******"
		./bfc -t $2 -c -i $TEST
		echo ""
		cd ..
	else
		echo "that folder does not exist"
	fi
	./clean.sh $1
else
	echo "Specify which folder to run the tests from"
fi
