import Testing
import RamaTcp

@Suite("RamaTcp Swift Export Tests")
struct RamaTcpExportTests {
    @Test("Swift module loads and exercises RamaTcp API")
    func swiftModuleLoads() {
        let version = RamaTcp.shared.VERSION
        #expect(!version.isEmpty)

        let stream = TcpStream.Companion.shared.new(stream: TokioTcpStream(localAddress: nil, peerAddress: nil))
        #expect(!stream.isWriteVectored())

        let builder = ExportedKotlinPackages.io.github.kotlinmania.ramatcp.server.TcpListener.Companion.shared.build().ttl(ttl: 64)
        #expect(builder.ttl == 64)
    }
}
