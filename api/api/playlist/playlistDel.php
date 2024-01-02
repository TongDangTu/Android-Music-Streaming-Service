<?php include "../../config/connection.php"; ?>
<?php include "../../model/account.php"; ?>
<?php
    header("Access-Control-Allow-Origin:*");
    header("Content-Type: application/json");
    header("Access-Control-Allow-Methods: POST");
    header("Access-Control-Allow-Headers: Access-Control-Allow-Headers, Content-Type, Access-Control-Allow-Methods, Authorization, X-Requested-With");

    if (empty($conn) == false) {
        $data_response = array('status' => false, 'data' => [], 'message' => "none");
        try {
            $data_request = json_decode(file_get_contents("php://input"));    
            $query1 =  "Delete from song_playlist WHERE id_playlist = ".$data_request->id;
            $query2 = "Delete from playlist WHERE id=".$data_request->id;
            // mysqli_stmt_bind_param($stmt, "ss", $data_request->name,$data_request->id);
            // mysqli_stmt_execute($stmt);
            // $result = mysqli_stmt_get_result($stmt);
                // Thực thi
                if ($conn->query($query1) == true) {
                    $data_response['status'] = true;
                    // $data_response['data'][] = array("name" => $data_request->name,"id"=> $data_request->id);
                    $data_response['message'] = "Xóa tất cả bài hát khỏi playlist có id là:".$data_request->id." thành công";
                    if($conn->query($query2) == true) {
                        $data_response['status'] = true;
                        $data_response['data'][] = array("id"=> $data_request->id);
                        $data_response['message'] = "Xóa playlist có id là:".$data_request->id." thành công";
                    }
                    else{
                        $data_response['status'] = false;
                        // $data_response['data'][] = array("name" => $data_request->name,"id"=> $data_request->id);
                        $data_response['message'] = "Xóa playlist có id là:".$data_request->id." thất bại";
                    }
                }
                else {
                    $data_response['status'] = false;
                    $data_response['message'] = "Xóa tất cả bài hát khỏi playlist thất bại";
                }
        }
        catch (Exception $ex) {
            $data_response['status'] = false;
            $data_response['message'] = "PHP: EXCEPTION code: ". $ex->getCode().". EXCEPTION message: ". $ex->getMessage().". File: ". $ex->getFile().". Line: ". $ex->getLine();
        }
        catch (Error $er) {
            $data_response['status'] = false;
            $data_response['message'] = "PHP: ERROR code: ". $er->getCode().". ERROR message: ". $er->getMessage().". File: ". $er->getFile().". Line: ". $er->getLine();
        }
        finally {
            // close prepare statement
            if (empty($stmt) == false) {
                try {
                    mysqli_stmt_close($stmt);
                }
                catch (Exception $ex) {
                    $data_response['status'] = false;
                    $data_response['message'] = "PHP: EXCEPTION code: ". $ex->getCode().". EXCEPTION message: ". $ex->getMessage().". File: ". $ex->getFile().". Line: ". $ex->getLine();
                }
                catch (Error $er) {
                    $data_response['status'] = false;
                    $data_response['message'] = "PHP: ERROR code: ". $er->getCode().". ERROR message: ". $er->getMessage().". File: ". $er->getFile().". Line: ". $er->getLine();
                }
            }
            // close connection
            if (empty($conn) == false) {
                try {
                    mysqli_close($conn);
                }
                catch (Exception $ex) {
                    $data_response['status'] = false;
                    $data_response['message'] = "PHP: EXCEPTION code: ". $ex->getCode().". EXCEPTION message: ". $ex->getMessage().". File: ". $ex->getFile().". Line: ". $ex->getLine();
                }
                catch (Error $er) {
                    $data_response['status'] = false;
                    $data_response['message'] = "PHP: ERROR code: ". $er->getCode().". ERROR message: ". $er->getMessage().". File: ". $er->getFile().". Line: ". $er->getLine();
                }
            }
        }
    }
    echo json_encode($data_response);
?>