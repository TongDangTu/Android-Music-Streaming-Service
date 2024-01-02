<?php include "../../config/connection.php"; ?>
<?php
    header("Access-Control-Allow-Origin:*");
    header("Content-Type: application/json");
    header("Access-Control-Allow-Methods: POST");
    header("Access-Control-Allow-Headers: Access-Control-Allow-Headers, Content-Type, Access-Control-Allow-Methods, Authorization, X-Requested-With");

    if (empty($conn) == false) {
        $data_response = array('status' => false, 'data' => [], 'message' => "none");
        try {
            $data_request = json_decode(file_get_contents("php://input"));
            $query = "SELECT id FROM playlist WHERE username = ? LIMIT 1";
            $stmt = mysqli_prepare($conn, $query);
            mysqli_stmt_bind_param($stmt, "s", $data_request->username);
            if (mysqli_stmt_execute($stmt)) {
                $result = mysqli_stmt_get_result($stmt);
                while ($row = mysqli_fetch_assoc($result)) {
                    $id_playlist = $row['id'];
                }
            }

            $query = "SELECT id FROM song_playlist ORDER BY id DESC LIMIT 1";
            $result = mysqli_query($conn, $query);
            if (mysqli_num_rows($result) > 0) {
                while ($row = mysqli_fetch_assoc($result)) {
                    $id = $row['id'];
                    $id = $id + 1;
                }
            }

            $stmt = mysqli_prepare($conn, "SELECT * FROM song_playlist WHERE id_song = ? AND id_playlist = ?");
            mysqli_stmt_bind_param($stmt, "ss", $data_request->id_song, $id_playlist);

            if (mysqli_stmt_execute($stmt)) {
                $result = mysqli_stmt_get_result($stmt);
                if (mysqli_num_rows($result) > 0) {
                    $data_response['status'] = false;
                    $data_response['message'] = "Bài hát đã tồn tại trong Favorite";
                }
                else {
                    $stmt = mysqli_prepare($conn, "INSERT INTO song_playlist VALUES(?, ?, ?)");
                    mysqli_stmt_bind_param($stmt, "sss", $id, $data_request->id_song, $id_playlist);
                    if (mysqli_stmt_execute($stmt)) {
                        $data_response['status'] = true;
                        $data_response['data'][] = array("id" => $id,
                                                        "id_song" => $data_request->id_song,
                                                        "id_playlist" => $id_playlist);
                        $data_response['message'] = "Thêm mới bài hát vào playlist favorite thành công";
                    }
                    else {
                        $data_response['status'] = false;
                        $data_response['message'] = "Thêm mới bài hát vào playlist favorite thất bại";
                    }
                }
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