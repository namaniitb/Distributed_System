/*
 * Automatically generated by jrpcgen 1.0.7 on 29.05.07 21:34
 * jrpcgen is part of the "Remote Tea" ONC/RPC package for Java
 * See http://remotetea.sourceforge.net for details
 */
package tests.org.acplt.oncrpc.jrpcgen;
import org.acplt.oncrpc.*;
import java.io.IOException;

public class TREE implements XdrAble, java.io.Serializable {
    protected String key;
    protected String value;
    protected TREE left;
    protected TREE right;

    private static final long serialVersionUID = 2403962346676670641L;

    public void setKey(String x) { this.key = x; }
    public String getKey() { return this.key; }

    public void setValue(String x) { this.value = x; }
    public String getValue() { return this.value; }

    public void setLeft(TREE x) { this.left = x; }
    public TREE getLeft() { return this.left; }

    public void setRight(TREE x) { this.right = x; }
    public TREE getRight() { return this.right; }

    public TREE() {
    }

    public TREE(XdrDecodingStream xdr)
           throws OncRpcException, IOException {
        xdrDecode(xdr);
    }

    public void xdrEncode(XdrEncodingStream xdr)
           throws OncRpcException, IOException {
        TREE $this = this;
        do {
            xdr.xdrEncodeString($this.key);
            xdr.xdrEncodeString($this.value);
            if ( $this.left != null ) { xdr.xdrEncodeBoolean(true); $this.left.xdrEncode(xdr); } else { xdr.xdrEncodeBoolean(false); };
            $this = $this.right;
            xdr.xdrEncodeBoolean($this != null);
        } while ( $this != null );
    }

    public void xdrDecode(XdrDecodingStream xdr)
           throws OncRpcException, IOException {
        TREE $this = this;
        TREE $next;
        do {
            $this.key = xdr.xdrDecodeString();
            $this.value = xdr.xdrDecodeString();
            $this.left = xdr.xdrDecodeBoolean() ? new TREE(xdr) : null;
            $next = xdr.xdrDecodeBoolean() ? new TREE() : null;
            $this.right = $next;
            $this = $next;
        } while ( $this != null );
    }

}
// End of TREE.java