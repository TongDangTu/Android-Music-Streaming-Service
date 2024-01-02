<?php
    header("Access-Control-Allow-Origin:*");
    header("Content-Type: application/json");
    $servername = "localhost";
    $database = "nghenhac";
    $username = "root";
    $password = "123456789";
    $data_response = array('status' => false, 'data' => [], 'message' => "none");
    try {
        // Create connection
        $conn = mysqli_connect($servername, $username, $password, $database);
        // Check connection
        if (!$conn) {
            die("Connection failed: " . mysqli_connect_error());
        }
        else {
            // echo '<script>console.log("Connection Successfuly"); </script>'; 
        }
    }
    catch (Exception $ex) {
        $data_response['status'] = false;
        $data_response['message'] = "PHP: EXCEPTION code: ". $ex->getCode().". EXCEPTION message: ". $ex->getMessage();
    }
    catch (Error $er) {
        $data_response['status'] = false;
        $data_response['message'] = "PHP: ERROR code: ". $er->getCode().". ERROR message: ". $er->getMessage();
    }
    
    if (empty($conn) == false) {
        $data_response = array('status' => false, 'data' => [], 'message' => "none");
        try {
            $stmt = mysqli_prepare($conn, "SELECT username, password FROM account WHERE username='tongdangtu' AND password='dangtu123' aLIMIT 1");
            mysqli_stmt_execute($stmt);
            // mysqli_stmt_bind_param("si", "haha", "hihi");
        }
        catch (Exception $ex) {
            $data_response['status'] = false;
            $data_response['message'] = "PHP: EXCEPTION code: ". $ex->getCode().". EXCEPTION message: ". $ex->getMessage();
        }
        catch (Error $er) {
            $data_response['status'] = false;
            $data_response['message'] = "PHP: ERROR code: ". $er->getCode().". ERROR message: ". $er->getMessage();
        }
        finally {
            if (empty($stmt) == false) {
                try {
                    mysqli_stmt_close($stmt);
                }
                catch (Exception $ex) {
                    $data_response['status'] = false;
                    $data_response['message'] = "PHP: EXCEPTION code: ". $ex->getCode().". EXCEPTION message: ". $ex->getMessage();
                }
                catch (Error $er) {
                    $data_response['status'] = false;
                    $data_response['message'] = "PHP: ERROR code: ". $er->getCode().". ERROR message: ". $er->getMessage();
                }
            }
            // close connection
            if (empty($conn) == false) {
                try {
                    mysqli_close($conn);
                }
                catch (Exception $ex) {
                    $data_response['status'] = false;
                    $data_response['message'] = "PHP: EXCEPTION code: ". $ex->getCode().". EXCEPTION message: ". $ex->getMessage();
                }
                catch (Error $er) {
                    $data_response['status'] = false;
                    $data_response['message'] = "PHP: ERROR code: ". $er->getCode().". ERROR message: ". $er->getMessage();
                }
            }
        }    
    }
    
    echo json_encode($data_response);
?>