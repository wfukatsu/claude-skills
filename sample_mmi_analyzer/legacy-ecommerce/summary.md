---
title: 評価サマリー - Legacy E-Commerce System
phase: "Phase 1: Investigation"
skill: mmi-evaluation
generated_at: 2026-02-10
project: legacy-ecommerce
project_path: /Users/wfukatsu/work/AIxDevOps/refactoring-agent/demo-project/legacy-ecommerce
---

# 評価サマリー - Legacy E-Commerce System

## エグゼクティブサマリー

Legacy E-Commerce System は Spring Boot 3.x + JPA ベースのモノリシックなECサイトバックエンドである。MMI評価の結果、総合スコア **45点（グレード D: 要改善）** と判定された。

特に以下の領域で深刻な問題が検出された:

1. **セキュリティ（Critical）**: カード番号の平文保存・ログ出力、認証機構の完全な欠如
2. **テスト（Critical）**: カバレッジ ~5%、テストファイル1つ、Happy path のみ
3. **設計（Major）**: レイヤー違反、循環依存、Anemic Domain Model、集約境界なし
4. **ドキュメント（Major）**: API仕様なし、Javadocなし

## スコアカード

| 評価軸 | スコア | 重み | 加重スコア | 判定 |
|--------|--------|------|-----------|------|
| 設計整合性 | 53 | 30% | 15.9 | 要改善 |
| テストカバレッジ | 35 | 25% | 8.75 | 危険 |
| ドキュメント | 25 | 20% | 5.0 | 危険 |
| コード品質 | 62 | 25% | 15.5 | 普通 |
| **総合MMI** | - | **100%** | **45** | **D（要改善）** |

## 課題統計

| 重要度 | 件数 | 主な内容 |
|--------|------|---------|
| Critical | 8 | セキュリティ(4)、設計(2)、データ整合性(1)、テスト(1) |
| Major | 10 | 設計(5)、品質(3)、ドキュメント(2) |
| Minor | 10 | 品質(5)、設計(4)、テスト(1) |
| **合計** | **28** | |

## 即座に対応すべき項目（Top 5）

1. **カード番号の暗号化/トークン化** — PCI DSS違反、情報漏洩リスク
2. **カード番号のログ出力除去** — 監査ログに機密情報が記録される
3. **Spring Security による認証・認可の導入** — 全エンドポイントが無防備
4. **Bean Validation による入力バリデーション** — インジェクション攻撃のリスク
5. **レイヤー違反の修正** — Controller → Repository の直接アクセスを排除

## 関連レポート

| ファイル | 内容 |
|----------|------|
| [`mmi-report.md`](mmi-report.md) | MMI 4軸評価の詳細スコアと課題一覧 |
| [`evaluation-report.md`](evaluation-report.md) | アーキテクチャ分析、セキュリティ評価、改善ロードマップ |

## 次のステップ

| 推奨スキル | 目的 |
|-----------|------|
| `/ddd-evaluation` | DDD戦略的・戦術的設計の詳細評価 |
| `/data-model-analysis` | データモデルの詳細分析とER図生成 |
| `/security-analysis` | OWASP Top 10 対応状況の包括的評価 |
| `/ddd-redesign` | DDD原則に基づくシステム再設計 |
