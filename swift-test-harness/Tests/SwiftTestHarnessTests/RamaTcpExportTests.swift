import Testing
import RamaTcp

@Suite("RamaTcp Swift Export Tests")
struct RamaTcpExportTests {
    @Test("Swift module loads and exercises RamaTcp API")
    func swiftModuleLoads() {
        let version = RamaTcp.shared.VERSION
        #expect(!version.isEmpty)

        let stream = TcpStream.companion.new(stream: TokioTcpStream(localAddress: nil, peerAddress: nil))
        #expect(!stream.isWriteVectored())

        let builder = TcpListener.companion.build().ttl(ttl: 64)
        #expect(builder.ttl == 64)
    }
}
