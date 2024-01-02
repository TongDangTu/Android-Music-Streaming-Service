<?php include "../../config/connection.php"; ?>
<?php include "../../model/playlist.php"; ?>
<?php
    header("Access-Control-Allow-Origin:*");
    header("Content-Type: application/json");

    if (empty($conn) == false) {
        $data_response = array('status' => false, 'data' => [], 'message' => "none");
        try {
            if (empty($_GET['id']) == false) {
                $str_id = $_GET['id'];
                $stmt =  "SELECT 
                s.id as id_song,s.name as name_song,s.playedTime as playedTime,s.linkSong as linkSong,s.linkPicture as linkPicture
                ,a.name as name_artist,c.name as name_category,p.id as playlistID
                FROM playlist p
                    INNER JOIN song_playlist sp ON p.id = sp.id_playlist
                        INNER JOIN song s ON s.id = sp.id_song
                            INNER JOIN song_category sc ON sc.id_song = s.id
                        INNER JOIN category c ON c.id = sc.id_category
                    INNER JOIN song_artist sa on sa.id_song = s.id 
                INNER JOIN artist a ON a.id = sa.id_artist
                where p.id=".$_GET['id'];
                // mysqli_stmt_bind_param($stmt, "s", $str_id);
                // mysqli_stmt_execute($stmt);
                // $result = mysqli_stmt_get_result($stmt);
                $result = mysqli_query($conn,$stmt);
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
                                $data_response['message'] = "Lấy danh sách phát thành công";
                            }
                        }
                    }
                    else {
                        $data_response['status'] = false;
                        $data_response['message'] = "Không có danh sách phát cần lấy";
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
            if (empty($stmt1) == false) {
                try {
                    mysqli_stmt_close($stmt1);
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