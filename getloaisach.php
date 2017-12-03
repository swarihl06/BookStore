<?php
	include "bookstore.php";
	$mangloaisach = array();
	$query = "SELECT * FROM loaisach";
 
// Kiểm tra kết nối
if ($conn->connect_error) {
    die("Kết nối thất bại: " . $conn->connect_error);
} 
 
// Câu SQL lấy danh sách
//$sql = "SELECT * FROM loaisach";
 
// Thực thi câu truy vấn và gán vào $result
//$data = $conn->query($query);
 
// Kiểm tra số lượng record trả về có lơn hơn 0
// Nếu lớn hơn tức là có kết quả, ngược lại sẽ không có kết quả
//if ($result->num_rows > 0) 
//{
    // Sử dụng vòng lặp while để lặp kết quả
//    while($row = $result->fetch_assoc()) {
//        echo json_encode("id: " . $row["id"]. " - tenloaisach: " . $row["tenloaisach"]." - hinhanhloaisach: ".$row["hinhanhloaisach"]);
//    }
//} 
//else {
//    echo "Không có record nào";
//}
 
// ngắt kết nối
//$conn->close();
	
	// LẤY ID, TÊN, HÌNH ẢNH

	$data = mysqli_query($conn, $query);
	while ($row = mysqli_fetch_assoc($data)) {
	 	array_push($mangloaisach, new Loaisach(
	 		$row['id'],
	 		$row['tenloaisach'],
	 		$row['hinhanhloaisach']));
	 	}
	 	echo json_encode($mangloaisach,JSON_UNESCAPED_UNICODE);
	 class Loaisach
	 {
	 	function __construct($id,$tenloaisach,$hinhanhloaisach){
	 		// echo $tenloaisach."<br>";
	 		// echo utf8_encode($tenloaisach)."<br>";
	 		$this->id=$id;
	 		$this->tenloaisach=$tenloaisach;
	 		$this->hinhanhloaisach=$hinhanhloaisach;
	 	}
	 }
?>