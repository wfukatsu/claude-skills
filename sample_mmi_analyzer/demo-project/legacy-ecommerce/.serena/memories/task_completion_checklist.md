# Task Completion Checklist

## When Analysis Task is Complete

### For MMI Evaluation
1. ✓ Generate report in `reports/before/legacy-ecommerce/03_mmi_evaluation.md`
2. ✓ Calculate scores for all 4 axes (design, tests, docs, code quality)
3. ✓ Provide weighted total score
4. ✓ Include issue severity classification (Critical/Major/Minor)

### For DDD Evaluation
1. ✓ Generate report in `reports/before/legacy-ecommerce/02_ddd_evaluation.md`
2. ✓ Evaluate strategic DDD (bounded contexts, context maps)
3. ✓ Evaluate tactical DDD (aggregates, entities, value objects)
4. ✓ Identify violations with code examples

### For System Investigation
1. ✓ Generate report in `reports/before/legacy-ecommerce/01_system_investigation.md`
2. ✓ Map directory structure
3. ✓ Identify dependencies
4. ✓ Analyze technology stack

## General Guidelines

### DO NOT
- Fix intentional technical debt in demo project
- Run linters/formatters (not configured)
- Modify code unless explicitly requested
- Push to git (this is local workshop material)

### DO
- Use Serena MCP for efficient analysis (symbol-based, not file reading)
- Generate reports in Japanese + English mixed format
- Include Mermaid diagrams where appropriate
- Save all outputs to `reports/` directory

## Report Quality Checks
- [ ] Executive summary (エグゼクティブサマリー) present
- [ ] Current state analysis (現状分析) with code examples
- [ ] Issues (課題) categorized by severity
- [ ] Recommendations (改善提案) prioritized
- [ ] Mermaid diagrams for architecture/flow visualization
- [ ] File saved to correct location with numbered prefix
