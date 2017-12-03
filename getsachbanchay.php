<?php
	include "bookstore.php";
	$mangsachmoi = array();
	$query = "SELECT * FROM sach ORDER BY ID LIMIT 8,6";

	$data = mysqli_query($conn, $query);
	while ($row = mysqli_fetch_assoc($data)) {
	 	array_push($mangsachmoi, new Sachmoi(
	 		$row['id'],
	 		$row['tensach'],
	 		$row['giasach'],
	 		$row['hinhanhsach'],
	 		$row['tacgia'],
	 		$row['motasach'],
	 		$row['idloaisach'],
	 		$row['ngayxuatbansach']
	 	));
	 	}
	 	 echo json_encode($mangsachmoi);
	 //	echo print_r($mangsachmoi);
	 class Sachmoi
	 {
	 	
	 	function __construct($id,$tensach,$giasach,$hinhanhsach,$tacgia,$motasach,$idloaisach,$ngayxuatbansach){
	 		// echo $tensach)."<br>";
	 		// echo $tensach."<br>";
	 		$this->id=$id;
	 		$this->tensach=$tensach;
	 		$this->giasach=$giasach;
	 		$this->hinhanhsach=$hinhanhsach;
	 		$this->tacgia=$tacgia;
	 		$this->motasach=$motasach;
	 		$this->idloaisach=$idloaisach;
	 		$this->ngayxuatbansach=$ngayxuatbansach;

	 	}
	 }
?>