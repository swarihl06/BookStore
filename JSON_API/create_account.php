<?php
	$response = array();
	if(isset($_REQUEST['uid']) && isset($_REQUEST['pass'])){
		$uid = $_REQUEST['uid'];
		$pass = $_REQUEST['pass'];
		$name = $_REQUEST['name'];
		$add = $_REQUEST['add'];

		require_once __DIR__.'/db_config.php';
		$con = mysqli_connect(DB_SERVER, DB_USER, DB_PASS, DB_NAME) or die('Error');
		$sql = "INSERT INTO User(UID, Password, Name, Address) VALUES ('$uid', '$pass', '$name', '$add')";

		$result = mysqli_query($con, $sql);

		if($result){
			$response["success"] = 1;
			echo json_encode($response);
		}
		else{
			$response["success"] = 0;
			echo json_encode($response);
		}
		mysqli_close($con);
	}
	else{
		$response["success"] = 0;
		echo json_encode($response);
	}

?>
