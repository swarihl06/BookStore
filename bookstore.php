<?php
	$host = "localhost";
	$username = "root"; // mặc định xampp
	$password = "";
	$database = "bookstore";

	$conn = mysqli_connect($host,$username,$password,$database); // câu lệnh kết nối
	mysqli_set_charset($conn,'utf8'); // định dạng kiểu chữ
	// echo "11";
?>