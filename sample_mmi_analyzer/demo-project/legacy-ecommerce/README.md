# Legacy E-Commerce System - デモ用サンプルプロジェクト

## 概要
このプロジェクトは、Claude Code リファクタリングエージェントのデモンストレーション用に作成されたレガシーECサイトのバックエンドシステムです。典型的な技術的負債と設計上の問題を含んでおり、リファクタリングの必要性を学ぶための教材として使用できます。

## システムの特徴

### ドメイン
- **注文管理**: 顧客からの注文を処理
- **在庫管理**: 商品の在庫を管理
- **顧客管理**: 顧客情報の登録・更新
- **決済処理**: クレジットカード決済の処理

### 技術スタック
- Java 17
- Spring Boot 3.x
- MySQL
- Maven

## 既知の問題点（リファクタリング対象）

### 1. DDD原則の欠如
- ✗ **境界コンテキストが不明確**: すべてのドメインロジックが単一のパッケージに混在
- ✗ **エンティティと値オブジェクトの区別がない**: すべてがエンティティとして実装
- ✗ **集約境界が不適切**: Order と OrderItem が独立して操作可能
- ✗ **ドメインサービスとアプリケーションサービスの混在**: ビジネスロジックがサービス層に散在

### 2. アーキテクチャの問題
- ✗ **レイヤー境界の侵害**: コントローラが直接リポジトリにアクセス
- ✗ **トランザクション境界が不適切**: 細かすぎる/粗すぎる処理が混在
- ✗ **循環依存**: OrderService ⇄ InventoryService

### 3. データモデルの問題
- ✗ **正規化不足**: 顧客の住所情報が注文テーブルに重複
- ✗ **不変性の欠如**: 注文確定後も金額が変更可能
- ✗ **外部キー制約の不足**: データ整合性がアプリケーション層に依存

### 4. コード品質の問題
- ✗ **テストカバレッジ不足**: ユニットテストが20%未満
- ✗ **長大なメソッド**: 100行を超えるメソッドが複数存在
- ✗ **重複コード**: 同様の処理が複数箇所にコピー&ペースト
- ✗ **マジックナンバー**: ハードコードされた定数が多数

### 5. セキュリティの問題
- ✗ **SQL インジェクション**: 一部のクエリが文字列結合で構築
- ✗ **認証・認可の不備**: すべてのエンドポイントが公開状態
- ✗ **機密情報のログ出力**: クレジットカード番号がログに記録

### 6. ドキュメントの問題
- ✗ **API ドキュメントの欠如**: OpenAPI 仕様がない
- ✗ **アーキテクチャドキュメントの欠如**: 設計意図が不明
- ✗ **コメント不足**: 複雑なロジックの説明がない

## ディレクトリ構造

```
legacy-ecommerce/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/example/ecommerce/
│   │   │       ├── controller/          # Web層（REST API）
│   │   │       │   ├── OrderController.java
│   │   │       │   ├── CustomerController.java
│   │   │       │   └── ProductController.java
│   │   │       ├── service/             # サービス層（問題あり）
│   │   │       │   ├── OrderService.java
│   │   │       │   ├── InventoryService.java
│   │   │       │   ├── CustomerService.java
│   │   │       │   └── PaymentService.java
│   │   │       ├── model/               # モデル層（エンティティのみ）
│   │   │       │   ├── Order.java
│   │   │       │   ├── OrderItem.java
│   │   │       │   ├── Customer.java
│   │   │       │   ├── Product.java
│   │   │       │   └── Payment.java
│   │   │       ├── repository/          # データアクセス層
│   │   │       │   ├── OrderRepository.java
│   │   │       │   ├── CustomerRepository.java
│   │   │       │   └── ProductRepository.java
│   │   │       └── util/                # ユーティリティ
│   │   │           ├── DateUtil.java
│   │   │           └── ValidationUtil.java
│   │   └── resources/
│   │       ├── application.properties
│   │       └── schema.sql
│   └── test/
│       └── java/                        # テストコード（不足）
├── pom.xml
└── README.md
```

## リファクタリング後の期待される構造（参考）

```
ecommerce-platform/
├── order-service/                       # 注文境界コンテキスト
│   ├── domain/
│   │   ├── model/
│   │   │   ├── Order.java              # 集約ルート
│   │   │   ├── OrderItem.java          # エンティティ
│   │   │   ├── OrderStatus.java        # 値オブジェクト
│   │   │   └── Money.java              # 値オブジェクト
│   │   ├── service/                     # ドメインサービス
│   │   └── event/                       # ドメインイベント
│   ├── application/                     # アプリケーションサービス
│   ├── infrastructure/                  # インフラ層（ScalarDB）
│   └── presentation/                    # REST API
├── inventory-service/                   # 在庫境界コンテキスト
├── customer-service/                    # 顧客境界コンテキスト
└── payment-service/                     # 決済境界コンテキスト
```

## セットアップ手順

### 前提条件
- Java 17+
- Maven 3.8+
- MySQL 8.0+（または Docker）

### 起動方法

1. **データベースの起動**（Docker を使用する場合）:
```bash
docker run -d \
  --name mysql-ecommerce \
  -e MYSQL_ROOT_PASSWORD=root \
  -e MYSQL_DATABASE=ecommerce \
  -p 3306:3306 \
  mysql:8.0
```

2. **アプリケーションのビルド**:
```bash
cd legacy-ecommerce
mvn clean install
```

3. **アプリケーションの起動**:
```bash
mvn spring-boot:run
```

4. **動作確認**:
```bash
curl http://localhost:8080/api/orders
```

## リファクタリングエージェントの実行手順

### Phase 1: 調査と分析

1. **システム調査**:
```bash
cd /path/to/legacy-ecommerce
claude /system-investigation
```

2. **DDD評価**:
```bash
claude /ddd-evaluation
```

3. **MMI品質評価**:
```bash
claude /mmi-evaluation
```

4. **統合評価レポート生成**:
```bash
claude /evaluation-report
```

### Phase 2: 設計

5. **DDD再設計**:
```bash
claude /ddd-redesign
```

6. **アーキテクチャ設計**:
```bash
claude /architecture-design
```

7. **データベース設計（ScalarDB）**:
```bash
claude /database-design
```

8. **実装計画策定**:
```bash
claude /implementation-planning
```

### Phase 3: レポート生成

9. **HTMLレポート生成**:
```bash
claude /html-report-generator
```

## 期待される成果物

実行後、以下のディレクトリ構造でレポートが生成されます:

```
reports/
├── before/
│   └── legacy-ecommerce/
│       ├── 01_system_investigation.md
│       ├── 02_ddd_evaluation.md
│       ├── 03_mmi_evaluation.md
│       └── evaluation_report.md
├── after/
│   └── legacy-ecommerce/
│       ├── 01_ddd_redesign/
│       ├── 02_architecture_design/
│       ├── 03_database_design/
│       ├── 04_api_design/
│       ├── 05_infrastructure_design/
│       └── implementation_plan.md
└── index.html
```

## 学習のポイント

### Session 1（分析）で注目すべき点
- Serena MCP がシンボルツリーをどう構築するか
- DDD評価で検出される具体的な問題箇所
- MMIスコアがどの軸で低いか

### Session 2（設計）で注目すべき点
- 境界コンテキストがどう分割されるか
- 集約の境界がどう設定されるか
- ScalarDB のスキーマ設計の考え方
- マイクロサービス分割の戦略

## トラブルシューティング

### Q: MySQL に接続できない
A: `application.properties` の接続情報を確認してください。

### Q: ビルドエラーが発生する
A: Java 17 以上を使用していることを確認してください。

### Q: Claude Code でプロジェクトを認識しない
A: プロジェクトルートで実行していることを確認してください。

## ライセンス
MIT License - 教育目的での利用を想定

## お問い合わせ
勉強会に関するご質問は [email] まで
