# Immediate Actions - High-Value Files

Based on AST analysis, here are the concrete next steps.

## Summary

- **Files Present:** 7/12 (58.3%)
- **Function parity:** 57/57 matched (target 128) — 100.0%
- **Class/type parity:** 21/21 matched (target 50) — 100.0%
- **Combined symbol parity:** 78/78 matched (target 178) — 100.0%
- **Average inline-code cosine:** 0.57 (function body across 7 matched files)
- **Average documentation cosine:** 0.59 (doc text across 7 matched files)
- **Cheat-zeroed Files:** 0
- **Critical Issues:** 4 files with <0.60 function similarity

## Priority 1: Fix Incomplete High-Dependency Files

No incomplete high-dependency files detected.

## Priority 2: Port Missing High-Value Files

Critical missing files (>10 dependencies):

No missing high-value files detected.

## Detailed Work Items

Every matched file is listed below with function and type symbol parity.

### 1. client.request

- **Target:** `client.Request`
- **Similarity:** 0.36
- **Dependents:** 1
- **Priority Score:** 1000806.4
- **Functions:** 6/6 matched (target 22)
- **Missing functions:** _none_
- **Types:** 2/2 matched (target 4)
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

### 3. stream

- **Target:** `ramatcp.Stream`
- **Similarity:** 0.65
- **Dependents:** 0
- **Priority Score:** 1203.5
- **Functions:** 12/12 matched (target 31)
- **Missing functions:** _none_
- **Types:** 0/0 matched (target 3)
- **Missing types:** _none_

### 4. client.connect

- **Target:** `client.Connect`
- **Similarity:** 0.58
- **Dependents:** 0
- **Priority Score:** 904.2
- **Functions:** 6/6 matched (target 14)
- **Missing functions:** _none_
- **Types:** 3/3 matched (target 13)
- **Missing types:** _none_

### 5. service.connector

- **Target:** `service.Connector`
- **Similarity:** 0.72
- **Dependents:** 0
- **Priority Score:** 902.8
- **Functions:** 6/6 matched (target 10)
- **Missing functions:** _none_
- **Types:** 3/3 matched (target 6)
- **Missing types:** _none_

### 6. service.forward

- **Target:** `service.Forward`
- **Similarity:** 0.82
- **Dependents:** 0
- **Priority Score:** 901.8
- **Functions:** 4/4 matched (target 6)
- **Missing functions:** _none_
- **Types:** 5/5 matched (target 10)
- **Missing types:** _none_

### 7. service.select

- **Target:** `service.Select`
- **Similarity:** 0.36
- **Dependents:** 0
- **Priority Score:** 606.4
- **Functions:** 1/1 matched (target 4)
- **Missing functions:** _none_
- **Types:** 5/5 matched (target 9)
- **Missing types:** _none_

## Success Criteria

For each file to be considered "complete":
- **Similarity ≥ 0.85** (Excellent threshold)
- All public APIs ported
- All tests ported
- Documentation ported
- port-lint header present

## Reexport / Wiring Modules

These files match `reexport_modules` patterns in `.ast_distance_config.json`. They are filtered out of
normal priority and missing-file ladders because they are wiring
modules, not direct logic ports. Consult them for call-site routing;
do not treat them as the next implementation target by default.

### Missing

| Source | Expected target | Deps | Source path | Expected path |
|--------|-----------------|------|-------------|---------------|
| `client.mod` | `client.Mod` | 0 | `client/mod.rs` | `client/Mod.kt` |
| `service.mod` | `client.service.Mod` | 0 | `client/service/mod.rs` | `client/service/Mod.kt` |
| `lib` | `Lib` | 0 | `lib.rs` | `Lib.kt` |
| `pool.mod` | `pool.Mod` | 0 | `pool/mod.rs` | `pool/Mod.kt` |
| `server.mod` | `server.Mod` | 0 | `server/mod.rs` | `server/Mod.kt` |

