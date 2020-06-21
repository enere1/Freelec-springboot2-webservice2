# SPRINGBOOTを利用するWEB SERIVCE
springbootとawsで開発からデプロイまで構築したプロジェクト

## 開発期間
20200515~20200621

## 開発同期
尊重しているプログラマーが本を出版し、やはりdeveloperというと開発から
デプロイまで経験が大事だと思ったから。

## GITHUB
https://github.com/enere1/Freelec-springboot2-webservice2

## ドメイン
https://www.leesungonboard.com/

## 開発

### step1
単位テストコード(UT)作成する事で得る利益  
①開発初期段階に問題を発見しやすくする。　

②今度コードをリファクタリングやライブラリアップグレード等に
既存機能がよく動くかわかれる。　

### step2
JPAを利用データベースを扱う  
JPA:ORMのスタンダード技術でHibernate、OpenJPA、EclipseLink、TopLink EssentialsなどImplementationがあり、このスタンダードインタフェースがJPAです。   
ORM(Object Relational Mapping)はRDBテーブルをオブジェクト指向プログラミングすることが出来る。  
①CRUDを使わなくてもいい  
->ビジネスロジックに集中さずにJAVAコードに集中出来る。  
②1:N関係、親ー子関係表現などクト指向プログラミングができる。

### step3
mustacheで画面構成  
mustacheはロジック使用が不可VIEWとSERVERの役割を明確に離れる

### step4
SpringセキュリティとOAuth2.0でローグイン機能追加

### step5
AWS EC2とRDSでサーバとデータベース環境構築

### step6
コードがプッシュされると自動にデプロイする。  
Travis CI利用　→　理由はJENKINSなどinstall系がありますが
このため、別のEC2インスタンスが必要からオープンソースを使う。  
実際はAWS CodeDeployがデプロイしますがセーブ機能がないため、取り使うように
保管するスペース役するAWS S3を使う。

### step7
CDを構築してデプロイしてもサービスが止まらないようにする。
Nginxを利用Apacheウェブサーバより高性能ウェブサーバです。
特にreverse proxyを利用してサーバ一つでNginx一つとspring boot jar二つを
使える。

###step8
ドメイン繋がり、aws certificate　managerで証明書を発行し、https protocolを使う。

## 全体構造
![overview](https://user-images.githubusercontent.com/50684536/85218929-0a94fc80-b3da-11ea-9f26-7c77586aea08.png)
