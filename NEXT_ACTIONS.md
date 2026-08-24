# Immediate Actions - High-Value Files

Based on AST analysis, here are the concrete next steps.

## Summary

- **Files Present:** 12/12 (100.0%)
- **Function parity:** 63/66 matched (target 120) — 95.5%
- **Class/type parity:** 15/24 matched (target 67) — 62.5%
- **Combined symbol parity:** 78/90 matched (target 187) — 86.7%
- **Average inline-code cosine:** 0.45 (function body across 8 matched files)
- **Average documentation cosine:** 0.60 (doc text across 8 matched files)
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
- **Similarity:** 0.44
- **Dependents:** 1
- **Priority Score:** 1020805.6
- **Functions:** 5/6 matched (target 11)
- **Missing functions:** `extensions`
- **Types:** 1/2 matched (target 6)
- **Missing types:** `Error`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `client/request.rs` vs expected `client/request.rs`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `tests:client/request.rs` vs expected `client/request.rs`
- **Proposed provenance header:** `// port-lint: source client/request.rs` (current: `// port-lint: source client/request.rs`)
- **Proposed provenance header:** `// port-lint: tests client/request.rs` (current: `// port-lint: tests client/request.rs`)
- **Lint issues:** 2

### 2. service.connector

- **Target:** `service.Connector [PROVENANCE-FALLBACK]`
- **Similarity:** 0.72
- **Dependents:** 0
- **Priority Score:** 20902.8
- **Functions:** 6/6 matched (target 10)
- **Missing functions:** _none_
- **Types:** 1/3 matched (target 8)
- **Missing types:** `Output`, `Error`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `client/service/connector.rs` vs expected `client/service/connector.rs`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `tests:client/service/connector.rs` vs expected `client/service/connector.rs`
- **Proposed provenance header:** `// port-lint: source client/service/connector.rs` (current: `// port-lint: source client/service/connector.rs`)
- **Proposed provenance header:** `// port-lint: tests client/service/connector.rs` (current: `// port-lint: tests client/service/connector.rs`)
- **Lint issues:** 2

### 3. service.forward

- **Target:** `service.Forward [PROVENANCE-FALLBACK]`
- **Similarity:** 0.81
- **Dependents:** 0
- **Priority Score:** 20901.9
- **Functions:** 4/4 matched (target 6)
- **Missing functions:** _none_
- **Types:** 3/5 matched (target 9)
- **Missing types:** `Output`, `Error`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `client/service/forward.rs` vs expected `client/service/forward.rs`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `tests:client/service/forward.rs` vs expected `client/service/forward.rs`
- **Proposed provenance header:** `// port-lint: source client/service/forward.rs` (current: `// port-lint: source client/service/forward.rs`)
- **Proposed provenance header:** `// port-lint: tests client/service/forward.rs` (current: `// port-lint: tests client/service/forward.rs`)
- **Lint issues:** 2

### 4. service.select

- **Target:** `service.Select [PROVENANCE-FALLBACK]`
- **Similarity:** 0.36
- **Dependents:** 0
- **Priority Score:** 20606.4
- **Functions:** 1/1 matched (target 2)
- **Missing functions:** _none_
- **Types:** 3/5 matched (target 6)
- **Missing types:** `Connector`, `Error`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `client/service/select.rs` vs expected `client/service/select.rs`
- **Proposed provenance header:** `// port-lint: source client/service/select.rs` (current: `// port-lint: source client/service/select.rs`)
- **Lint issues:** 1

### 5. server.listener

- **Target:** `server.Listener [PROVENANCE-FALLBACK]`
- **Similarity:** 0.45
- **Dependents:** 0
- **Priority Score:** 12505.5
- **Functions:** 21/22 matched (target 28)
- **Missing functions:** `local_addr`
- **Types:** 3/3 matched (target 4)
- **Missing types:** _none_
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `server/listener.rs` vs expected `server/listener.rs`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `tests:server/listener.rs` vs expected `server/listener.rs`
- **Proposed provenance header:** `// port-lint: source server/listener.rs` (current: `// port-lint: source server/listener.rs`)
- **Proposed provenance header:** `// port-lint: tests server/listener.rs` (current: `// port-lint: tests server/listener.rs`)
- **Lint issues:** 3

### 6. pool.mod

- **Target:** `pool.Mod [STUB] [PROVENANCE-FALLBACK]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 11210.0
- **Functions:** 9/9 matched (target 12)
- **Missing functions:** _none_
- **Types:** 2/3 matched (target 7)
- **Missing types:** `Error`
- **Tests:** 5/5 matched
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `pool/mod.rs` vs expected `pool/mod.rs`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `tests:pool/mod.rs` vs expected `pool/mod.rs`
- **Proposed provenance header:** `// port-lint: source pool/mod.rs` (current: `// port-lint: source pool/mod.rs`)
- **Proposed provenance header:** `// port-lint: tests pool/mod.rs` (current: `// port-lint: tests pool/mod.rs`)
- **Lint issues:** 2

### 7. stream

- **Target:** `ramatcp.Stream [PROVENANCE-FALLBACK]`
- **Similarity:** 0.36
- **Dependents:** 0
- **Priority Score:** 11206.4
- **Functions:** 11/12 matched (target 38)
- **Missing functions:** `extensions`
- **Types:** 0/0 matched (target 12)
- **Missing types:** _none_
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `stream.rs` vs expected `stream.rs`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `tests:stream.rs` vs expected `stream.rs`
- **Proposed provenance header:** `// port-lint: source stream.rs` (current: `// port-lint: source stream.rs`)
- **Proposed provenance header:** `// port-lint: tests stream.rs` (current: `// port-lint: tests stream.rs`)
- **Lint issues:** 2

### 8. client.connect

- **Target:** `client.Connect [PROVENANCE-FALLBACK]`
- **Similarity:** 0.43
- **Dependents:** 0
- **Priority Score:** 10905.7
- **Functions:** 6/6 matched (target 12)
- **Missing functions:** _none_
- **Types:** 2/3 matched (target 10)
- **Missing types:** `Error`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `client/connect.rs` vs expected `client/connect.rs`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `tests:client/connect.rs` vs expected `client/connect.rs`
- **Proposed provenance header:** `// port-lint: source client/connect.rs` (current: `// port-lint: source client/connect.rs`)
- **Proposed provenance header:** `// port-lint: tests client/connect.rs` (current: `// port-lint: tests client/connect.rs`)
- **Lint issues:** 3

### 9. lib

- **Target:** `ramatcp.Lib [ZERO] [PROVENANCE-FALLBACK]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 10.0
- **Functions:** 0/0 matched (target 1)
- **Missing functions:** _none_
- **Types:** 0/0 matched (target 2)
- **Missing types:** _none_
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `lib.rs` vs expected `lib.rs`
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `tests:lib.rs` vs expected `lib.rs`
- **Proposed provenance header:** `// port-lint: source lib.rs` (current: `// port-lint: source lib.rs`)
- **Proposed provenance header:** `// port-lint: tests lib.rs` (current: `// port-lint: tests lib.rs`)
- **Lint issues:** 2

### 10. server.mod

- **Target:** `server.Mod [STUB] [PROVENANCE-FALLBACK]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 10.0
- **Functions:** 0/0 matched
- **Missing functions:** _none_
- **Types:** 0/0 matched (target 1)
- **Missing types:** _none_
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `server/mod.rs` vs expected `server/mod.rs`
- **Proposed provenance header:** `// port-lint: source server/mod.rs` (current: `// port-lint: source server/mod.rs`)
- **Lint issues:** 1

### 11. client.mod

- **Target:** `client.Mod [STUB] [PROVENANCE-FALLBACK]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 10.0
- **Functions:** 0/0 matched
- **Missing functions:** _none_
- **Types:** 0/0 matched (target 1)
- **Missing types:** _none_
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `client/mod.rs` vs expected `client/mod.rs`
- **Proposed provenance header:** `// port-lint: source client/mod.rs` (current: `// port-lint: source client/mod.rs`)
- **Lint issues:** 1

### 12. service.mod

- **Target:** `service.Mod [STUB] [PROVENANCE-FALLBACK]`
- **Similarity:** 0.00
- **Dependents:** 0
- **Priority Score:** 10.0
- **Functions:** 0/0 matched
- **Missing functions:** _none_
- **Types:** 0/0 matched (target 1)
- **Missing types:** _none_
- **Provenance warning:** port-lint provenance header matched only after fallback normalization: `client/service/mod.rs` vs expected `client/service/mod.rs`
- **Proposed provenance header:** `// port-lint: source client/service/mod.rs` (current: `// port-lint: source client/service/mod.rs`)
- **Lint issues:** 1

## Success Criteria

For each file to be considered "complete":
- **Similarity ≥ 0.85** (Excellent threshold)
- All public APIs ported
- All tests ported
- Documentation ported
- port-lint header present

