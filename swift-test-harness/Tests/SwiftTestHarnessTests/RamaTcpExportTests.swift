import Testing
import RamaTcp

@Suite("RamaTcp Swift Export Tests")
struct RamaTcpExportTests {
    @Test("Swift module loads and accesses RamaTcp version")
    func swiftModuleLoads() {
        let version = RamaTcp.shared.VERSION
        #expect(!version.isEmpty)
    }
}
