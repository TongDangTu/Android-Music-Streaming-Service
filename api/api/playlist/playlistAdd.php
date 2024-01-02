<?php include "../../config/connection.php"; ?>
<?php include "../../model/account.php"; ?>
<?php
    header("Access-Control-Allow-Origin:*");
    header("Content-Type: application/json");
    header("Access-Control-Allow-Methods: POST");
    header("Access-Control-Allow-Headers: Access-Control-Allow-Headers, Content-Type, Access-Control-Allow-Methods, Authorization, X-Requested-With");

    if (empty($conn) == false) {
        $query1 = "Select id from playlist order by id desc limit 1";
        $rs = mysqli_query($conn, $query1);
        if (mysqli_num_rows($rs) > 0) {
            while ($row = mysqli_fetch_assoc($rs)) {
                $id = $row["id"];
            }
        }
        $id = (int) $id;
        $id = $id + 1;
        $id = strval($id);
        $data_response = array('status' => false, 'data' => [], 'message' => "none");
        try {
            $data_request = json_decode(file_get_contents("php://input"));    
            $stmt = mysqli_prepare($conn,"INSERT INTO playlist (id,name,username) VALUES (?,?,?)");
            mysqli_stmt_bind_param($stmt, "sss", $id,$data_request->name, $data_request->username);
            $result = mysqli_stmt_execute($stmt);
            if ($result) {
                $data_response['status'] = true;
                $data_response['message'] = "Thêm Playlist thành công";
            }
            else{
                $data_response["status"] = false;
                $data_response["message"] = "Thêm playlist thất bại";
            }
            // $stmt = mysqli_prepare($conn, "SELECT username FROM account WHERE username=? LIMIT 1");
            // mysqli_stmt_bind_param($stmt, "s", $data_request->username);
            // mysqli_stmt_execute($stmt);
            // $result = mysqli_stmt_get_result($stmt);
            // if (mysqli_num_rows($result) > 0) {
            //     while ($row = mysqli_fetch_assoc($result)) {
            //         $data_response['status'] = false;
            //         $data_response['message'] = "Tài khoản đã tồn tại";
            //     }
            // }
            // else {
            //     $stmt = mysqli_prepare($conn, "INSERT INTO account VALUES(?, ?)");
            //     // Truyền giá trị cho các param
            //     mysqli_stmt_bind_param($stmt, "ss", $data_request->username, $data_request->password);
            //     // Thực thi
            //     if (mysqli_stmt_execute($stmt)) {
            //         $data_response['status'] = true;
            //         $data_response['data'][] = array("username" => $data_request->username);
            //         $data_response['message'] = "Đăng ký tài khoản mới thành công";
            //     }
            //     else {
            //         $data_response['status'] = false;
            //         $data_response['message'] = "Đăng ký tài khoản mới thất bại";
            //     }
            // }
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