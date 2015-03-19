<?php
    //generic php function to send GCM push notification
   function sendPushNotificationToGCM($registatoin_ids, $title, $message) {
        //Google cloud messaging GCM-API url
        $url = 'https://android.googleapis.com/gcm/send';
        $fields = array(
            'registration_ids' => $registatoin_ids,
            'data' => array("title" => $title, "message" => $message),
        );
        // Google Cloud Messaging GCM API Key
        define("GOOGLE_API_KEY", "AIzaSyDVIthQ3_uofF64IWEvzdOeHFS2eAuXo9w");
        $headers = array(
            'Authorization: key=' . GOOGLE_API_KEY,
            'Content-Type: application/json'
        );
        $ch = curl_init();
        curl_setopt($ch, CURLOPT_URL, $url);
        curl_setopt($ch, CURLOPT_POST, true);
        curl_setopt($ch, CURLOPT_HTTPHEADER, $headers);
        curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
        curl_setopt ($ch, CURLOPT_SSL_VERIFYHOST, 0);
        curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, false);
        curl_setopt($ch, CURLOPT_POSTFIELDS, json_encode($fields));
        $result = curl_exec($ch);
        if ($result === FALSE) {
            die('Curl failed: ' . curl_error($ch));
        }
        curl_close($ch);
        return $result;
    }
?>
<?php

    //this block is to post message to GCM on-click
    $pushStatus = "";
    if(!empty($_GET["push"])) {
        // $gcmRegID  = file_get_contents("GCMRegId.txt");
        $pushTitle = $_POST["title"];
        $pushMessage = $_POST["message"];
        // if (isset($gcmRegID) && isset($pushMessage)) {
        if (isset($pushMessage)) {
            $gcmRegIds = array("APA91bErJNT7Q28Ta-Vga4sgI9Xs6Fl1SRaTSqTyAcgHm4hoF6lzAh-emFDoJF1lCLgvHd-2dLOr4lBfsxlTaV6HTozeMNKUtCB79ww1pUujCkFppwS17GGnQb1sbDljiqnxRDddeI6qj8ECPqvMu4C9R-21KTQ5exvYkPDaMccfdW3dSN37Jt331HuSZuocYkDmMPwM5zwv", "APA91bFWjqJhehbV6glvq0bkgmHbN8kNX5Zh6ieoTKN_xnrn9PH0viQ5R1Y-bXB-QVV4VPmCbnpS2xbmGBScAGqnAtqjeuY3dII8RpIVBy5Wjh5WpApct-hDD_jNe08POTmp55uWhprh31MPn8g-j-LTCEVBdNRIRPT8NimTMCHN1C4tc4NHcN5tZaykYqTH0feCYmbo86yv");
            $message = $pushMessage;
            $title = $pushTitle;
            $pushStatus = sendPushNotificationToGCM($gcmRegIds, $title, $message);
        }
    }

    //this block is to receive the GCM regId from external (mobile apps)
    if(!empty($_GET["shareRegId"])) {
        $gcmRegID  = $_POST["regId"];
        // file_put_contents("GCMRegId.txt",$gcmRegID);
        // echo "Ok!";
        exit;
    }
?>
<html>
    <head>
        <meta charset="utf8">
        <title>Google Cloud Messaging (GCM) Server in PHP</title>
    </head>
    <body>
        <h1>Google Cloud Messaging (GCM) Server in PHP</h1>
        <form method="post" action="push_notification_gae.php/?push=1">
            <div>
                <input type="text" name="title" placeholder="Push通知タイトル">
            </div>
            <br>
            <div>
                <textarea rows="2" name="message" cols="23" placeholder="Push通知メッセージ"></textarea>
            </div>
            <div><input type="submit"  value="Push送信" /></div>
        </form>
        <p><h3><?php echo $pushStatus; ?></h3></p>
    </body>
</html>