<?php
    // $servername = "localhost";
    // $database = "id21518078_nghenhac";
    // $username = "id21518078_id21518078_tongdangtu";
    // $password = "Hoangtu2002*";
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
        $data_response['message'] = "PHP: EXCEPTION code: ". $ex->getCode().". EXCEPTION message: ". $ex->getMessage().". File: ". $ex->getFile().". Line: ". $ex->getLine();
    }
    catch (Error $er) {
        $data_response['status'] = false;
        $data_response['message'] = "PHP: ERROR code: ". $er->getCode().". ERROR message: ". $er->getMessage().". File: ". $er->getFile().". Line: ". $er->getLine();
    }
?>
