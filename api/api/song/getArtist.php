<?php include "../../config/connection.php"; ?>
<?php include "../../model/artist.php"; ?>
<?php include "../../model/song.php"; ?>
<?php
    header("Access-Control-Allow-Origin:*");
    header("Content-Type: application/json");

    if (empty($conn) == false) {
        $data_response = array('status' => false, 'data' => [], 'message' => "none");
        try {
            if (empty($_GET['id']) == false) {
                $str_id = $_GET['id'];
                $stmt = mysqli_prepare($conn, "SELECT * FROM song WHERE id=? LIMIT 1");
                mysqli_stmt_bind_param($stmt, "s", $str_id);
                mysqli_stmt_execute($stmt);
                $result = mysqli_stmt_get_result($stmt);
                if (mysqli_num_rows($result) > 0) {
                    while ($row = mysqli_fetch_assoc($result)) {
                        $stmt = mysqli_prepare($conn, "SELECT artist.id AS id, artist.name, artist.linkPicture
                                                        FROM artist INNER JOIN song_artist ON artist.id=song_artist.id_artist
                                                        INNER JOIN song ON song_artist.id_song = song.id 
                                                        WHERE song.id=?");
                        mysqli_stmt_bind_param($stmt, "s", $str_id);
                        mysqli_stmt_execute($stmt);
                        $result = mysqli_stmt_get_result($stmt);
                        if (mysqli_num_rows($result) > 0) {
                            while ($row = mysqli_fetch_assoc($result)) {
                                $artist = new Artist ($row['id'], $row['name'], $row['linkPicture']);
                                $data_response['status'] = true;
                                $data_response['data'][] = array("id" => $artist->getId(),
                                                        "name" => $artist->getName(),
                                                        "linkPicture" => $artist->getLinkPicture());
                                $data_response['message'] = "Lấy nghệ sĩ của bài hát thành công";
                            }
                        }
                        else {
                            $data_response['status'] = false;
                            $data_response['message'] = "Bài hát này không có nghệ sĩ";
                        }
                    }
                }
                else {
                    $data_response['status'] = false;
                    $data_response['message'] = "Không có bài hát cần lấy";
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