# Immediate Actions - High-Value Files

Based on AST analysis, here are the concrete next steps.

## Summary

- **Files Present:** 12/12 (100.0%)
- **Function parity:** 66/66 matched (target 141) — 100.0%
- **Class/type parity:** 24/24 matched (target 69) — 100.0%
- **Combined symbol parity:** 90/90 matched (target 210) — 100.0%
- **Average inline-code cosine:** 0.46 (function body across 8 matched files)
- **Average documentation cosine:** 0.61 (doc text across 8 matched files)
- **Cheat-zeroed Files:** 6
- **Critical Issues:** 9 files with <0.60 function similarity

## Priority 1: Fix Incomplete High-Dependency Files

No incomplete high-dependency files detected.

## Priority 2: Port Missing High-Value Files

Critical missing files (>10 dependencies):

No missing high-value files detected.

## Detailed Work Items

Every matched file is listed below with function and type symbol parity.

### 1. client.request

- **Target:** `client.Request [ZERO]`
- **Similarity:** 0.00
- **Dependents:** 1
- **Priority Score:** 1000810.0
- **Functions:** 6/6 matched (target 22)
- **Missing functions:** _none_
- **Types:** 2/2 matched (target 5)
- **Missing types:** _none_

### 2. server.listener

- **Target:** `server.Listener`
- **Similarity:** 0.53
- **Dependents:** 0
- **Priority Score:** 2504.7
- **Functions:** 22/22 matched (target 41)
- **Missing functions:** _none_
- **Types:** 3/3 matched (target 5)
- **Missing types:** _none_

### 3. pool.mod

- **Target:** `pool.Mod [STUB]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 1210.0
- **Functions:** 9/9 matched (target 12)
- **Missing functions:** _none_
- **Types:** 3/3 matched (target 8)
- **Missing types:** _none_
- **Tests:** 5/5 matched

### 4. stream

- **Target:** `ramatcp.Stream`
- **Similarity:** 0.65
- **Dependents:** 0
- **Priority Score:** 1203.5
- **Functions:** 12/12 matched (target 31)
- **Missing functions:** _none_
- **Types:** 0/0 matched (target 3)
- **Missing types:** _none_

### 5. client.connect

- **Target:** `client.Connect`
- **Similarity:** 0.58
- **Dependents:** 0
- **Priority Score:** 904.2
- **Functions:** 6/6 matched (target 14)
- **Missing functions:** _none_
- **Types:** 3/3 matched (target 13)
- **Missing types:** _none_

### 6. service.connector

- **Target:** `service.Connector`
- **Similarity:** 0.72
- **Dependents:** 0
- **Priority Score:** 902.8
- **Functions:** 6/6 matched (target 10)
- **Missing functions:** _none_
- **Types:** 3/3 matched (target 6)
- **Missing types:** _none_

### 7. service.forward

- **Target:** `service.Forward`
- **Similarity:** 0.82
- **Dependents:** 0
- **Priority Score:** 901.8
- **Functions:** 4/4 matched (target 6)
- **Missing functions:** _none_
- **Types:** 5/5 matched (target 10)
- **Missing types:** _none_

### 8. service.select

- **Target:** `service.Select`
- **Similarity:** 0.36
- **Dependents:** 0
- **Priority Score:** 606.4
- **Functions:** 1/1 matched (target 4)
- **Missing functions:** _none_
- **Types:** 5/5 matched (target 9)
- **Missing types:** _none_

### 9. lib

- **Target:** `ramatcp.Lib [ZERO]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 10.0
- **Functions:** 0/0 matched (target 1)
- **Missing functions:** _none_
- **Types:** 0/0 matched (target 4)
- **Missing types:** _none_

### 10. server.mod

- **Target:** `server.Mod [STUB]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 10.0
- **Functions:** 0/0 matched
- **Missing functions:** _none_
- **Types:** 0/0 matched (target 2)
- **Missing types:** _none_

### 11. client.mod

- **Target:** `client.Mod [STUB]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 10.0
- **Functions:** 0/0 matched
- **Missing functions:** _none_
- **Types:** 0/0 matched (target 2)
- **Missing types:** _none_

### 12. service.mod

- **Target:** `service.Mod [STUB]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 10.0
- **Functions:** 0/0 matched
- **Missing functions:** _none_
- **Types:** 0/0 matched (target 2)
- **Missing types:** _none_

## Success Criteria

For each file to be considered "complete":
- **Similarity ≥ 0.85** (Excellent threshold)
- All public APIs ported
- All tests ported
- Documentation ported
- port-lint header present

