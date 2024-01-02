<?php include "../../config/connection.php"; ?>
<?php include "../../model/playlist.php"; ?>
<?php
    header("Access-Control-Allow-Origin:*");
    header("Content-Type: application/json");

    if (empty($conn) == false) {
        $data_response = array('status' => false, 'data' => [], 'message' => "none");
        try {
            if (empty($_GET['username']) == false) {
                $str_id = $_GET['username'];
                
                $query = "SELECT id FROM playlist WHERE username = ? LIMIT 1";
                $stmt = mysqli_prepare($conn, $query);
                mysqli_stmt_bind_param($stmt, "s", $str_id);
                if (mysqli_stmt_execute($stmt)) {
                    $result = mysqli_stmt_get_result($stmt);
                    while ($row = mysqli_fetch_assoc($result)) {
                        $id_playlist = $row['id'];
                    }
                }
                
                $stmt = mysqli_prepare($conn, "SELECT 
                p.id AS playlist_id,
                p.name AS playlist_name,
                MAX(s.linkPicture) AS playlist_picture
                    FROM playlist p
                        LEFT JOIN song_playlist sp ON p.id = sp.id_playlist
                            LEFT JOIN song s ON s.id = sp.id_song
                        WHERE p.username = ? AND p.id != ?
                    GROUP BY p.id, p.name;");
                mysqli_stmt_bind_param($stmt, "ss", $str_id, $id_playlist);
                mysqli_stmt_execute($stmt);
                $result = mysqli_stmt_get_result($stmt);
                if (mysqli_num_rows($result) > 0) {
                    while ($row = mysqli_fetch_assoc($result)) {
                        if($row['playlist_picture'] == null || $row['playlist_picture'] == ""){
                            $row['playlist_picture'] = "https://nhomhungtu.000webhostapp.com/img/img_cd.png";
                        }
                        $data_response['status'] = true;
                        $data_response['data'][] = array("id" => $row['playlist_id'],
                                                        "name" => $row['playlist_name'],
                                                        "linkPicture" => $row['playlist_picture']);
                        $data_response['message'] = "Lấy danh sách phát thành công";
                    }
                }
                else {
                    $data_response['status'] = false;
                    $data_response['message'] = "Không có danh sách phát cần lấy";
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