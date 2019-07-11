
	var fruitsname =["apple","orange"];
	fruitsname.unshift("grapes"); //adds at the begining
	console.log("adds element at the beginning",fruitsname);

	fruitsname.push("mango");//adds at the last
	console.log("adds at the last",fruitsname)

	fruitsname.shift();//deletes the first element
	console.log("after deleting first element",fruitsname);

	fruitsname.pop()//deletes the element from last
	console.log("deletes element from last",fruitsname);

	fruitsname.splice(0,1); //used for deleting specific element
	console.log("using splice method deleting element",fruitsname);

	fruitsname.splice(0,0,"grapes","Mango");
	console.log("using splice adding elements",fruitsname);

	var arr = [1,2,3,4,5,6]; //for iterating each element
	arr.forEach(item=>{
		console.log(item);
	})

	const numbers =[1,2,3,4,5,6];
	console.log(numbers.includes(2));
	console.log(numbers.includes(7));

	var number = [1,2,3,4,5,6];
	var filterednumbers = number.filter(result=>result > 4);//checks the condition and stores the passed values
	console.log("using filter method",filterednumbers);

	var arraymap = [1,2,3,4,5,6]; //performs action on every element that presents in function
	var resultedarraymap = arr.map(number=>number+1);
	console.log("using map method",resultedarraymap);

	var resultedsome = arr.some(result=>result >4);//atleast one should pass the condition
	console.log("using some() ",resultedsome);

	var resultevery  = arr.every(result=>result>4);//every element should pass the condition
	console.log("using every() ",resultevery);

	var sortedOrder = arr.sort();
	console.log("sorted elements of array"sortedOrder);

	


