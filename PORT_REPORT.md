=== Deep Analysis: tmp/rama-tcp (rust) -> src/commonMain/kotlin (kotlin) ===

Scanning source codebase (rust)...
Codebase: tmp/rama-tcp (rust)
  Files: 12
  Total imports: 64
  Most depended: client.request (1 dependents)

Scanning target codebase (kotlin)...
Codebase: src/commonMain/kotlin (kotlin)
  Files: 21
  Total imports: 90
  Most depended: client.Request (3 dependents)

Comparing codebases...
Computing AST similarities...

=== Codebase Comparison Report ===

Source: tmp/rama-tcp (12 files)
Target: src/commonMain/kotlin (21 files)
Scoring invariant: FnSim is required function body/parameter similarity. Class/type and symbol parity are reported beside it; whole-file shape is diagnostic only.

Matched:   12 files
Unmatched: 0 source, 1 target

=== Matched Files (by porting priority) ===

Source                        Target                        FnSim     Dependents FunctionParityTypeParity  Priority
 --------------------------------------------------------------------------------------------------------------------------
client.request                client.Request [PROVENANCE-FALLBACK]0.44      1          5/6           1/2         1020805.6 
service.connector             service.Connector [PROVENANCE-FALLBACK]0.72      0          6/6           1/3         20902.8   
service.forward               service.Forward [PROVENANCE-FALLBACK]0.81      0          4/4           3/5         20901.9   
service.select                service.Select [PROVENANCE-FALLBACK]0.36      0          1/1           3/5         20606.4   
server.listener               server.Listener [PROVENANCE-FALLBACK]0.45      0          21/22         3/3         12505.5   
pool.mod                      pool.Mod [STUB] [PROVENANCE-FALLBACK]0.00      0          9/9           2/3         11210.0   
stream                        ramatcp.Stream [PROVENANCE-FALLBACK]0.36      0          11/12         0/0         11206.4   
client.connect                client.Connect [PROVENANCE-FALLBACK]0.43      0          6/6           2/3         10905.7   
lib                           ramatcp.Lib [ZERO] [PROVENANCE-FALLBACK]0.00      0          0/0           0/0         10.0      
server.mod                    server.Mod [STUB] [PROVENANCE-FALLBACK]0.00      0          0/0           0/0         10.0      
client.mod                    client.Mod [STUB] [PROVENANCE-FALLBACK]0.00      0          0/0           0/0         10.0      
service.mod                   service.Mod [STUB] [PROVENANCE-FALLBACK]0.00      0          0/0           0/0         10.0      

=== Function and Symbol Details ===

client.request -> client.Request [PROVENANCE-FALLBACK]
  similarity: 0.44, priority: 1020805.6, dependents: 1
  provenance warning: port-lint provenance header matched only after fallback normalization: `client/request.rs` vs expected `client/request.rs`
  provenance warning: port-lint provenance header matched only after fallback normalization: `tests:client/request.rs` vs expected `client/request.rs`
  functions: 5/6 matched (target total: 11, required body score: 0.44)
  missing functions: extensions
  types: 1/2 matched (target total: 6)
  missing types: Error

service.connector -> service.Connector [PROVENANCE-FALLBACK]
  similarity: 0.72, priority: 20902.8, dependents: 0
  provenance warning: port-lint provenance header matched only after fallback normalization: `client/service/connector.rs` vs expected `client/service/connector.rs`
  provenance warning: port-lint provenance header matched only after fallback normalization: `tests:client/service/connector.rs` vs expected `client/service/connector.rs`
  functions: 6/6 matched (target total: 10, required body score: 0.72)
  missing functions: none
  types: 1/3 matched (target total: 8)
  missing types: Output, Error

service.forward -> service.Forward [PROVENANCE-FALLBACK]
  similarity: 0.81, priority: 20901.9, dependents: 0
  provenance warning: port-lint provenance header matched only after fallback normalization: `client/service/forward.rs` vs expected `client/service/forward.rs`
  provenance warning: port-lint provenance header matched only after fallback normalization: `tests:client/service/forward.rs` vs expected `client/service/forward.rs`
  functions: 4/4 matched (target total: 6, required body score: 0.81)
  missing functions: none
  types: 3/5 matched (target total: 9)
  missing types: Output, Error

service.select -> service.Select [PROVENANCE-FALLBACK]
  similarity: 0.36, priority: 20606.4, dependents: 0
  provenance warning: port-lint provenance header matched only after fallback normalization: `client/service/select.rs` vs expected `client/service/select.rs`
  functions: 1/1 matched (target total: 2, required body score: 0.36)
  missing functions: none
  types: 3/5 matched (target total: 6)
  missing types: Connector, Error

server.listener -> server.Listener [PROVENANCE-FALLBACK]
  similarity: 0.45, priority: 12505.5, dependents: 0
  provenance warning: port-lint provenance header matched only after fallback normalization: `server/listener.rs` vs expected `server/listener.rs`
  provenance warning: port-lint provenance header matched only after fallback normalization: `tests:server/listener.rs` vs expected `server/listener.rs`
  functions: 21/22 matched (target total: 28, required body score: 0.45)
  missing functions: local_addr
  types: 3/3 matched (target total: 4)
  missing types: none

pool.mod -> pool.Mod [STUB] [PROVENANCE-FALLBACK]
  similarity: 0.00, priority: 11210.0, dependents: 0
  provenance warning: port-lint provenance header matched only after fallback normalization: `pool/mod.rs` vs expected `pool/mod.rs`
  provenance warning: port-lint provenance header matched only after fallback normalization: `tests:pool/mod.rs` vs expected `pool/mod.rs`
  functions: 9/9 matched (target total: 12, required body score: 0.00)
  missing functions: none
  types: 2/3 matched (target total: 7)
  missing types: Error
  *** CHEAT DETECTION / SCORING FAILURE ***
  function-by-function score forced to 0: target contains TODO/stub/placeholder markers in function bodies
  tests: 5/5 matched

stream -> ramatcp.Stream [PROVENANCE-FALLBACK]
  similarity: 0.36, priority: 11206.4, dependents: 0
  provenance warning: port-lint provenance header matched only after fallback normalization: `stream.rs` vs expected `stream.rs`
  provenance warning: port-lint provenance header matched only after fallback normalization: `tests:stream.rs` vs expected `stream.rs`
  functions: 11/12 matched (target total: 38, required body score: 0.36)
  missing functions: extensions
  types: 0/0 matched (target total: 12)
  missing types: none

client.connect -> client.Connect [PROVENANCE-FALLBACK]
  similarity: 0.43, priority: 10905.7, dependents: 0
  provenance warning: port-lint provenance header matched only after fallback normalization: `client/connect.rs` vs expected `client/connect.rs`
  provenance warning: port-lint provenance header matched only after fallback normalization: `tests:client/connect.rs` vs expected `client/connect.rs`
  functions: 6/6 matched (target total: 12, required body score: 0.43)
  missing functions: none
  types: 2/3 matched (target total: 10)
  missing types: Error

lib -> ramatcp.Lib [ZERO] [PROVENANCE-FALLBACK]
  similarity: 0.00, priority: 10.0, dependents: 0
  provenance warning: port-lint provenance header matched only after fallback normalization: `lib.rs` vs expected `lib.rs`
  provenance warning: port-lint provenance header matched only after fallback normalization: `tests:lib.rs` vs expected `lib.rs`
  functions: 0/0 matched (target total: 1, required body score: 0.00)
  missing functions: none
  types: 0/0 matched (target total: 2)
  missing types: none
  *** CHEAT DETECTION / SCORING FAILURE ***
  function-by-function score forced to 0: no source functions found; target defines functions; report scoring is function-by-function only

server.mod -> server.Mod [STUB] [PROVENANCE-FALLBACK]
  similarity: 0.00, priority: 10.0, dependents: 0
  provenance warning: port-lint provenance header matched only after fallback normalization: `server/mod.rs` vs expected `server/mod.rs`
  functions: 0/0 matched (target total: 0, required body score: 0.00)
  missing functions: none
  types: 0/0 matched (target total: 1)
  missing types: none
  *** CHEAT DETECTION / SCORING FAILURE ***
  function-by-function score forced to 0: target contains TODO/stub/placeholder markers in function bodies

client.mod -> client.Mod [STUB] [PROVENANCE-FALLBACK]
  similarity: 0.00, priority: 10.0, dependents: 0
  provenance warning: port-lint provenance header matched only after fallback normalization: `client/mod.rs` vs expected `client/mod.rs`
  functions: 0/0 matched (target total: 0, required body score: 0.00)
  missing functions: none
  types: 0/0 matched (target total: 1)
  missing types: none
  *** CHEAT DETECTION / SCORING FAILURE ***
  function-by-function score forced to 0: target contains TODO/stub/placeholder markers in function bodies

service.mod -> service.Mod [STUB] [PROVENANCE-FALLBACK]
  similarity: 0.00, priority: 10.0, dependents: 0
  provenance warning: port-lint provenance header matched only after fallback normalization: `client/service/mod.rs` vs expected `client/service/mod.rs`
  functions: 0/0 matched (target total: 0, required body score: 0.00)
  missing functions: none
  types: 0/0 matched (target total: 1)
  missing types: none
  *** CHEAT DETECTION / SCORING FAILURE ***
  function-by-function score forced to 0: target contains TODO/stub/placeholder markers in function bodies


=== Scores Forced To 0 ===

  - pool.mod -> pool.Mod: target contains TODO/stub/placeholder markers in function bodies
  - lib -> ramatcp.Lib: no source functions found; target defines functions; report scoring is function-by-function only
  - server.mod -> server.Mod: target contains TODO/stub/placeholder markers in function bodies
  - client.mod -> client.Mod: target contains TODO/stub/placeholder markers in function bodies
  - service.mod -> service.Mod: target contains TODO/stub/placeholder markers in function bodies

=== Provenance Header Fallbacks ===

These files were paired only after normalization; fix the port-lint source header.
  - client.request -> client.Request: port-lint provenance header matched only after fallback normalization: `client/request.rs` vs expected `client/request.rs`
    proposed: // port-lint: source client/request.rs
  - client.request -> client.Request: port-lint provenance header matched only after fallback normalization: `tests:client/request.rs` vs expected `client/request.rs`
    proposed: // port-lint: tests client/request.rs
  - service.connector -> service.Connector: port-lint provenance header matched only after fallback normalization: `client/service/connector.rs` vs expected `client/service/connector.rs`
    proposed: // port-lint: source client/service/connector.rs
  - service.connector -> service.Connector: port-lint provenance header matched only after fallback normalization: `tests:client/service/connector.rs` vs expected `client/service/connector.rs`
    proposed: // port-lint: tests client/service/connector.rs
  - service.forward -> service.Forward: port-lint provenance header matched only after fallback normalization: `client/service/forward.rs` vs expected `client/service/forward.rs`
    proposed: // port-lint: source client/service/forward.rs
  - service.forward -> service.Forward: port-lint provenance header matched only after fallback normalization: `tests:client/service/forward.rs` vs expected `client/service/forward.rs`
    proposed: // port-lint: tests client/service/forward.rs
  - service.select -> service.Select: port-lint provenance header matched only after fallback normalization: `client/service/select.rs` vs expected `client/service/select.rs`
    proposed: // port-lint: source client/service/select.rs
  - server.listener -> server.Listener: port-lint provenance header matched only after fallback normalization: `server/listener.rs` vs expected `server/listener.rs`
    proposed: // port-lint: source server/listener.rs
  - server.listener -> server.Listener: port-lint provenance header matched only after fallback normalization: `tests:server/listener.rs` vs expected `server/listener.rs`
    proposed: // port-lint: tests server/listener.rs
  - pool.mod -> pool.Mod: port-lint provenance header matched only after fallback normalization: `pool/mod.rs` vs expected `pool/mod.rs`
    proposed: // port-lint: source pool/mod.rs
  - pool.mod -> pool.Mod: port-lint provenance header matched only after fallback normalization: `tests:pool/mod.rs` vs expected `pool/mod.rs`
    proposed: // port-lint: tests pool/mod.rs
  - stream -> ramatcp.Stream: port-lint provenance header matched only after fallback normalization: `stream.rs` vs expected `stream.rs`
    proposed: // port-lint: source stream.rs
  - stream -> ramatcp.Stream: port-lint provenance header matched only after fallback normalization: `tests:stream.rs` vs expected `stream.rs`
    proposed: // port-lint: tests stream.rs
  - client.connect -> client.Connect: port-lint provenance header matched only after fallback normalization: `client/connect.rs` vs expected `client/connect.rs`
    proposed: // port-lint: source client/connect.rs
  - client.connect -> client.Connect: port-lint provenance header matched only after fallback normalization: `tests:client/connect.rs` vs expected `client/connect.rs`
    proposed: // port-lint: tests client/connect.rs
  - lib -> ramatcp.Lib: port-lint provenance header matched only after fallback normalization: `lib.rs` vs expected `lib.rs`
    proposed: // port-lint: source lib.rs
  - lib -> ramatcp.Lib: port-lint provenance header matched only after fallback normalization: `tests:lib.rs` vs expected `lib.rs`
    proposed: // port-lint: tests lib.rs
  - server.mod -> server.Mod: port-lint provenance header matched only after fallback normalization: `server/mod.rs` vs expected `server/mod.rs`
    proposed: // port-lint: source server/mod.rs
  - client.mod -> client.Mod: port-lint provenance header matched only after fallback normalization: `client/mod.rs` vs expected `client/mod.rs`
    proposed: // port-lint: source client/mod.rs
  - service.mod -> service.Mod: port-lint provenance header matched only after fallback normalization: `client/service/mod.rs` vs expected `client/service/mod.rs`
    proposed: // port-lint: source client/service/mod.rs

=== Porting Quality Summary ===

Matched by exact header:          0 / 12
Matched by provenance fallback:   12 / 12
Matched by name:                  0 / 12
Total TODOs in target: 0
Total lint errors:    22
Stub files:           4

=== Big Picture ===

- Missing files: 0
- Incomplete ports (similarity < 60%): 10
- Stub files: 4
- Files missing functions: 3 (total deficit: 3 functions)
- Type definitions missing: 9
- Files missing tests: 0 (total deficit: 0 unported `#[test]` functions)
- Documentation coverage: 388 / 384 lines (101%)

Primary focus: replace stub files with real implementations

=== Files with Issues ===

File                          Similarity LineRatio  FunctionParityTests     TODOs Lint  Status
----------------------------------------------------------------------------------------------------
client.Request [PROVENANCE-F  0.44       0.00       5/6           -         0     2     MISSING_FUNCS
  missing functions: `extensions`
  missing types: `Error`
service.Connector [PROVENANC  0.72       0.00       6/6           -         0     2     MISSING_TYPES
  missing types: `Output`, `Error`
service.Forward [PROVENANCE-  0.81       0.00       4/4           -         0     2     MISSING_TYPES
  missing types: `Output`, `Error`
service.Select [PROVENANCE-F  0.36       0.00       1/1           -         0     1     LOW_SIM
  missing types: `Connector`, `Error`
server.Listener [PROVENANCE-  0.45       0.00       21/22         -         0     3     MISSING_FUNCS
  missing functions: `local_addr`
pool.Mod [STUB] [PROVENANCE-  0.00       0.00       9/9           5/5       0     2     STUB
  missing types: `Error`
ramatcp.Stream [PROVENANCE-F  0.36       0.00       11/12         -         0     2     LOW_SIM
  missing functions: `extensions`
client.Connect [PROVENANCE-F  0.43       0.00       6/6           -         0     3     MISSING_TYPES
  missing types: `Error`
ramatcp.Lib [ZERO] [PROVENAN  0.00       0.00       -             -         0     2     LOW_SIM
server.Mod [STUB] [PROVENANC  0.00       0.00       -             -         0     1     STUB
client.Mod [STUB] [PROVENANC  0.00       0.00       -             -         0     1     STUB
service.Mod [STUB] [PROVENAN  0.00       0.00       -             -         0     1     STUB

=== Porting Recommendations ===

Incomplete ports (similarity < 60%): 10
Missing files: 0

Incomplete ports to complete:
  client.request                 similarity=0.44 function_parity=5/6 dependents=1
    missing functions: `extensions`
    missing types: `Error`
  service.select                 similarity=0.36 function_parity=1/1 dependents=0
    missing types: `Connector`, `Error`
  server.listener                similarity=0.45 function_parity=21/22 dependents=0
    missing functions: `local_addr`
  pool.mod                       similarity=0.00 function_parity=9/9 dependents=0 [STUB]
    missing types: `Error`
  stream                         similarity=0.36 function_parity=11/12 dependents=0
    missing functions: `extensions`
  client.connect                 similarity=0.43 function_parity=6/6 dependents=0
    missing types: `Error`
  lib                            similarity=0.00 function_parity=- dependents=0
  server.mod                     similarity=0.00 function_parity=- dependents=0 [STUB]
  client.mod                     similarity=0.00 function_parity=- dependents=0 [STUB]
  service.mod                    similarity=0.00 function_parity=- dependents=0 [STUB]

=== Documentation Gaps ===

Documentation coverage: 388 / 384 lines (101%)
Files with >20% doc gap: 4

File                          Src Docs    Tgt Docs    Gap %     DocSim    DocAmt    DocEq     
----------------------------------------------------------------------------------------------
server.mod                    78          15          80%       0.60      0.19      0.40      
server.listener               140         80          42%       0.68      0.57      0.62      
service.select                58          28          51%       0.64      0.48      0.56      
lib                           20          11          45%       0.95      0.55      0.75      

=== Generating Reports ===

✅ Generated: port_status_report.md
✅ Generated: high_priority_ports.md
✅ Generated: NEXT_ACTIONS.md
✅ Generated: port_lint_proposed_changes.md

📁 All reports generated successfully!
