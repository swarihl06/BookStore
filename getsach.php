<?php
	include "bookstore.php";
	$page = $_GET['page'];
	$idls = $_POST['idloaisach'];
	$manghinhanhtheoloai = array();
	$query = "SELECT * FROM sach WHERE idloaisach = $idls";

	$data = mysqli_query($conn, $query);

	while ($row = mysqli_fetch_assoc($data)) {
		// echo $row['hinhanhsach'];
	 	array_push($manghinhanhtheoloai, new hinhanhsachtheoloai(
	 		$row['id'],
	 		$row['hinhanhsach'],
	 		$row['idloaisach'],
	 		$row['tensach'],
	 		$row['giasach'],
	 		$row['tacgia'],
	 		$row['motasach'],
	 		$row['ngayxuatbansach']
	 	));
	 	}

	 	echo json_encode($manghinhanhtheoloai);

	class hinhanhsachtheoloai
	 {
	 	
	 	function __construct($id,$hinhanhsach,$idloaisach,$tensach,$giasach,$tacgia,$motasach,$ngayxuatbansach){
	 		$this->id=$id; // nay ha ok 
	 		$this->hinhanhsach=$hinhanhsach;
	 		$this->idloaisach=$idloaisach;
	 		$this->tensach=$tensach;
	 		$this->giasach=$giasach;
	 		$this->tacgia=$tacgia;
	 		$this->motasach=$motasach;
	 		$this->ngayxuatbansach=$ngayxuatbansach;
	 	}
	 }

?>