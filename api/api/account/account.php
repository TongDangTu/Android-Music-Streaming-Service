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
            $stmt = mysqli_prepare($conn, "SELECT username, password FROM account WHERE username=? AND password=? LIMIT 1");
            // Truyền giá trị cho các param
            mysqli_stmt_bind_param($stmt, "ss", $data_request->username, $data_request->password);
            // Thực thi
            mysqli_stmt_execute($stmt);
            // Cách này không kiểm tra được kết quả có bao nhiêu bản ghi
            // Ánh xạ các trường của kết quả sang $username_account và $password_account
            // mysqli_stmt_bind_result($stmt, $username_account, $password_account);

            // $data_response = array('status' => false, 'message' => "");
            // while (mysqli_stmt_fetch($stmt)) {
            //     $data_response['status'] = true;
            //     $data_response['message'] = "Tài khoản hoặc mật khẩu chính xác". $username_account;
            // }

            // Lấy kết quả (cách này nếu muốn kiểm tra xem kết quả có bao nhiêu bản ghi)
            $result = mysqli_stmt_get_result($stmt);
            if (mysqli_num_rows($result) > 0) {
                while ($row = mysqli_fetch_assoc($result)) {
                    $data_response['status'] = true;
                    $data_response['data'][] = array("username" => $row['username']);
                    $data_response['message'] = "Tài tài khoản, mật khẩu chính xác";
                }
            }
            else {
                $data_response['status'] = false;
                $data_response['message'] = "Tên tài khoản hoặc mật khẩu không chính xác";
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