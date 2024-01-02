<?php include "../../config/connection.php"; ?>
<?php
    header("Access-Control-Allow-Origin:*");
    header("Content-Type: application/json");
    
    if (empty($conn) == false) {
        $data_response = array('status' => false, 'data' => [], 'message' => "none");
        try {
            if (empty($_GET['search']) == false) {
                $search =  $_GET['search'];
                $search = "%$search%";
                $query = "SELECT song.id AS id_song, song.name AS name_song, song.playedTime AS playedTime, song.linkSong AS linkSong, song.linkPicture AS linkPicture,
                                artist.name AS name_artist, category.name AS name_category
                            FROM song LEFT JOIN song_artist ON song.id=song_artist.id_song
                                        LEFT JOIN artist ON song_artist.id_artist = artist.id
                                        LEFT JOIN song_category ON song.id = song_category.id_song
                                        LEFT JOIN category ON song_category.id_category = category.id
                            WHERE song.name LIKE ? OR artist.name LIKE ? OR category.name LIKE ?
                            ORDER BY playedTime DESC LIMIT 20";
                $stmt = mysqli_prepare($conn, $query);
                mysqli_stmt_bind_param($stmt, "sss", $search, $search, $search);
                mysqli_stmt_execute($stmt);

                $result = mysqli_stmt_get_result($stmt);
                if (mysqli_num_rows($result) > 0) {
                    while ($row = mysqli_fetch_assoc($result)) {
                        $id_song = $row['id_song'];
                        $name_song = $row['name_song'];
                        $playedTime = $row['playedTime'];
                        $linkSong = $row['linkSong'];
                        $linkPicture = $row['linkPicture'];
                        $name_artist = $row['name_artist'];
                        $name_category = $row['name_category'];

                        $data_response['status'] = true;
                        $data_response['data'][] = array("id_song" => $id_song,
                                                        "name_song" => $name_song,
                                                        "playedTime" => $playedTime,
                                                        "linkSong" => $linkSong,
                                                        "linkPicture" => $linkPicture,
                                                        "name_artist" => $name_artist,
                                                        "name_category" => $name_category);
                        $data_response['message'] = "Lấy danh sách tìm kiếm thành công";
                    }
                }
                else {
                    $data_response['status'] = false;
                    $data_response['message'] = "Không tìm thấy kết quả phù hợp";
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