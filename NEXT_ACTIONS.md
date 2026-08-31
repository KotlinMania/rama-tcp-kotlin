# Immediate Actions - High-Value Files

Based on AST analysis, here are the concrete next steps.

## Summary

- **Files Present:** 12/12 (100.0%)
- **Function parity:** 66/66 matched (target 134) — 100.0%
- **Class/type parity:** 24/24 matched (target 85) — 100.0%
- **Combined symbol parity:** 90/90 matched (target 219) — 100.0%
- **Average inline-code cosine:** 0.50 (function body across 7 matched files)
- **Average documentation cosine:** 0.55 (doc text across 7 matched files)
- **Cheat-zeroed Files:** 5
- **Critical Issues:** 10 files with <0.60 function similarity

## Priority 1: Fix Incomplete High-Dependency Files

No incomplete high-dependency files detected.

## Priority 2: Port Missing High-Value Files

Critical missing files (>10 dependencies):

No missing high-value files detected.

## Detailed Work Items

Every matched file is listed below with function and type symbol parity.

### 1. client.request

- **Target:** `client.Request [PROVENANCE-FALLBACK]`
- **Similarity:** 0.37
- **Dependents:** 1
- **Priority Score:** 1000806.3
- **Functions:** 6/6 matched (target 20)
- **Missing functions:** _none_
- **Types:** 2/2 matched (target 8)
- **Missing types:** _none_
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `rama-tcp/src/client/request.rs` vs expected `client/request.rs`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `tests:rama-tcp/src/client/request.rs` vs expected `client/request.rs`
- **Proposed provenance header:** `// port-lint: source client/request.rs` (current: `// port-lint: source rama-tcp/src/client/request.rs`)
- **Proposed provenance header:** `// port-lint: tests client/request.rs` (current: `// port-lint: tests rama-tcp/src/client/request.rs`)
- **Lint issues:** 2

### 2. server.listener

- **Target:** `server.Listener [PROVENANCE-FALLBACK]`
- **Similarity:** 0.46
- **Dependents:** 0
- **Priority Score:** 2505.4
- **Functions:** 22/22 matched (target 30)
- **Missing functions:** _none_
- **Types:** 3/3 matched (target 5)
- **Missing types:** _none_
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `rama-tcp/src/server/listener.rs` vs expected `server/listener.rs`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `tests:rama-tcp/src/server/listener.rs` vs expected `server/listener.rs`
- **Proposed provenance header:** `// port-lint: source server/listener.rs` (current: `// port-lint: source rama-tcp/src/server/listener.rs`)
- **Proposed provenance header:** `// port-lint: tests server/listener.rs` (current: `// port-lint: tests rama-tcp/src/server/listener.rs`)
- **Lint issues:** 2

### 3. pool.mod

- **Target:** `pool.Mod [STUB] [PROVENANCE-FALLBACK]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 1210.0
- **Functions:** 9/9 matched (target 12)
- **Missing functions:** _none_
- **Types:** 3/3 matched (target 8)
- **Missing types:** _none_
- **Tests:** 5/5 matched
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `rama-tcp/src/pool/mod.rs` vs expected `pool/mod.rs`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `tests:rama-tcp/src/pool/mod.rs` vs expected `pool/mod.rs`
- **Proposed provenance header:** `// port-lint: source pool/mod.rs` (current: `// port-lint: source rama-tcp/src/pool/mod.rs`)
- **Proposed provenance header:** `// port-lint: tests pool/mod.rs` (current: `// port-lint: tests rama-tcp/src/pool/mod.rs`)
- **Lint issues:** 2

### 4. stream

- **Target:** `ramatcp.Stream [PROVENANCE-FALLBACK]`
- **Similarity:** 0.32
- **Dependents:** 0
- **Priority Score:** 1206.8
- **Functions:** 12/12 matched (target 39)
- **Missing functions:** _none_
- **Types:** 0/0 matched (target 12)
- **Missing types:** _none_
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `rama-tcp/src/stream.rs` vs expected `stream.rs`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `tests:rama-tcp/src/stream.rs` vs expected `stream.rs`
- **Proposed provenance header:** `// port-lint: source stream.rs` (current: `// port-lint: source rama-tcp/src/stream.rs`)
- **Proposed provenance header:** `// port-lint: tests stream.rs` (current: `// port-lint: tests rama-tcp/src/stream.rs`)
- **Lint issues:** 2

### 5. client.connect

- **Target:** `client.Connect [PROVENANCE-FALLBACK]`
- **Similarity:** 0.43
- **Dependents:** 0
- **Priority Score:** 905.7
- **Functions:** 6/6 matched (target 12)
- **Missing functions:** _none_
- **Types:** 3/3 matched (target 12)
- **Missing types:** _none_
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `rama-tcp/src/client/connect.rs` vs expected `client/connect.rs`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `tests:rama-tcp/src/client/connect.rs` vs expected `client/connect.rs`
- **Proposed provenance header:** `// port-lint: source client/connect.rs` (current: `// port-lint: source rama-tcp/src/client/connect.rs`)
- **Proposed provenance header:** `// port-lint: tests client/connect.rs` (current: `// port-lint: tests rama-tcp/src/client/connect.rs`)
- **Lint issues:** 2

### 6. service.connector

- **Target:** `service.Connector [PROVENANCE-FALLBACK]`
- **Similarity:** 0.72
- **Dependents:** 0
- **Priority Score:** 902.8
- **Functions:** 6/6 matched (target 10)
- **Missing functions:** _none_
- **Types:** 3/3 matched (target 10)
- **Missing types:** _none_
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `rama-tcp/src/client/service/connector.rs` vs expected `client/service/connector.rs`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `tests:rama-tcp/src/client/service/connector.rs` vs expected `client/service/connector.rs`
- **Proposed provenance header:** `// port-lint: source client/service/connector.rs` (current: `// port-lint: source rama-tcp/src/client/service/connector.rs`)
- **Proposed provenance header:** `// port-lint: tests client/service/connector.rs` (current: `// port-lint: tests rama-tcp/src/client/service/connector.rs`)
- **Lint issues:** 2

### 7. service.forward

- **Target:** `service.Forward [PROVENANCE-FALLBACK]`
- **Similarity:** 0.81
- **Dependents:** 0
- **Priority Score:** 901.9
- **Functions:** 4/4 matched (target 6)
- **Missing functions:** _none_
- **Types:** 5/5 matched (target 11)
- **Missing types:** _none_
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `rama-tcp/src/client/service/forward.rs` vs expected `client/service/forward.rs`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `tests:rama-tcp/src/client/service/forward.rs` vs expected `client/service/forward.rs`
- **Proposed provenance header:** `// port-lint: source client/service/forward.rs` (current: `// port-lint: source rama-tcp/src/client/service/forward.rs`)
- **Proposed provenance header:** `// port-lint: tests client/service/forward.rs` (current: `// port-lint: tests rama-tcp/src/client/service/forward.rs`)
- **Lint issues:** 2

### 8. service.select

- **Target:** `service.Select [PROVENANCE-FALLBACK]`
- **Similarity:** 0.36
- **Dependents:** 0
- **Priority Score:** 606.4
- **Functions:** 1/1 matched (target 4)
- **Missing functions:** _none_
- **Types:** 5/5 matched (target 9)
- **Missing types:** _none_
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `rama-tcp/src/client/service/select.rs` vs expected `client/service/select.rs`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `tests:rama-tcp/src/client/service/select.rs` vs expected `client/service/select.rs`
- **Proposed provenance header:** `// port-lint: source client/service/select.rs` (current: `// port-lint: source rama-tcp/src/client/service/select.rs`)
- **Proposed provenance header:** `// port-lint: tests client/service/select.rs` (current: `// port-lint: tests rama-tcp/src/client/service/select.rs`)
- **Lint issues:** 2

### 9. lib

- **Target:** `ramatcp.Lib [STUB] [PROVENANCE-FALLBACK]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 10.0
- **Functions:** 0/0 matched (target 1)
- **Missing functions:** _none_
- **Types:** 0/0 matched (target 4)
- **Missing types:** _none_
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `rama-tcp/src/lib.rs` vs expected `lib.rs`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `tests:rama-tcp/src/lib.rs` vs expected `lib.rs`
- **Proposed provenance header:** `// port-lint: source lib.rs` (current: `// port-lint: source rama-tcp/src/lib.rs`)
- **Proposed provenance header:** `// port-lint: tests lib.rs` (current: `// port-lint: tests rama-tcp/src/lib.rs`)
- **Lint issues:** 2

### 10. server.mod

- **Target:** `server.Mod [STUB] [PROVENANCE-FALLBACK]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 10.0
- **Functions:** 0/0 matched
- **Missing functions:** _none_
- **Types:** 0/0 matched (target 2)
- **Missing types:** _none_
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `rama-tcp/src/server/mod.rs` vs expected `server/mod.rs`
- **Proposed provenance header:** `// port-lint: source server/mod.rs` (current: `// port-lint: source rama-tcp/src/server/mod.rs`)
- **Lint issues:** 1

### 11. client.mod

- **Target:** `client.Mod [STUB] [PROVENANCE-FALLBACK]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 10.0
- **Functions:** 0/0 matched
- **Missing functions:** _none_
- **Types:** 0/0 matched (target 2)
- **Missing types:** _none_
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `rama-tcp/src/client/mod.rs` vs expected `client/mod.rs`
- **Proposed provenance header:** `// port-lint: source client/mod.rs` (current: `// port-lint: source rama-tcp/src/client/mod.rs`)
- **Lint issues:** 1

### 12. service.mod

- **Target:** `service.Mod [STUB] [PROVENANCE-FALLBACK]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 10.0
- **Functions:** 0/0 matched
- **Missing functions:** _none_
- **Types:** 0/0 matched (target 2)
- **Missing types:** _none_
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `rama-tcp/src/client/service/mod.rs` vs expected `client/service/mod.rs`
- **Proposed provenance header:** `// port-lint: source client/service/mod.rs` (current: `// port-lint: source rama-tcp/src/client/service/mod.rs`)
- **Lint issues:** 1

## Success Criteria

For each file to be considered "complete":
- **Similarity ≥ 0.85** (Excellent threshold)
- All public APIs ported
- All tests ported
- Documentation ported
- port-lint header present

