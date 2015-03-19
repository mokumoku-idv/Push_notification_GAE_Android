# Push_notification_GAE_Android
GAEを使用してAndroidアプリにPush通知を送るサンプル

## Push通知の送信方法

全方法で端末毎のregistrtion_idが必要になる  
(2015/3/19 現在、registration_idは手動で設定しなければいけない。  
**Todo** アプリを起動するとregistration_idが登録されているか・最新のregistration_idかをチェックし、  
登録されていない端末であれば登録する処理の追加)

1. ターミナルでcurlを叩く  

    ``$ curl --header "Authorization: key=＜API Key＞" --header Content-Type:"application/json" https://android.googleapis.com/gcm/send  -d "{\"registration_ids\":[\"＜デバイス毎のregistration_ids＞\"],\"data\":{\"message\":\"Hello World\"}}"``

    例  
    ``curl --header "Authorization: key=AIzaSyDVIthQ3_uofF64IWEvzdOeHFS2eAuXo9w" --header Content-Type:"application/json" https://android.googleapis.com/gcm/send  -d "{\"registration_ids\":[\"APA91bErJNT7Q28Ta-Vga4sgI9Xs6Fl1SRaTSqTyAcgHm4hoF6lzAh-emFDoJF1lCLgvHd-2dLOr4lBfsxlTaV6HTozeMNKUtCB79ww1pUujCkFppwS17GGnQb1sbDljiqnxRDddeI6qj8ECPqvMu4C9R-21KTQ5exvYkPDaMccfdW3dSN37Jt331HuSZuocYkDmMPwM5zwv\", \"APA91bFWjqJhehbV6glvq0bkgmHbN8kNX5Zh6ieoTKN_xnrn9PH0viQ5R1Y-bXB-QVV4VPmCbnpS2xbmGBScAGqnAtqjeuY3dII8RpIVBy5Wjh5WpApct-hDD_jNe08POTmp55uWhprh31MPn8g-j-LTCEVBdNRIRPT8NimTMCHN1C4tc4NHcN5tZaykYqTH0feCYmbo86yv\"],\"data\":{\"title\":\"test\", \"message\":\"Hello World\"}}"``  

2. GCMのサンプルを使用  

    * GCMようにプロジェクトに追加したモジュール（このプロジェクトでは**backend**）を起動して、  
    localhost:8080をブラウザで開くとGCMのサンプルページが開き、Push通知を送信することが可能  

3. phpで実行

    **push_notification_by_GUI**にサンプルコードをおいているが、  
    json形式のデータをcurlで叩くプログラムを作成し、実行することでPush通知を送信可能
