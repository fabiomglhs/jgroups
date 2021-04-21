package pt.ipb.dsys.peerbox;

import org.jgroups.JChannel;
import org.jgroups.ObjectMessage;
import org.jgroups.conf.PropertyConverters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.ipb.dsys.peerbox.jgroups.DefaultProtocols;
import pt.ipb.dsys.peerbox.jgroups.LoggingReceiver;
import pt.ipb.dsys.peerbox.util.Sleeper;

public class Main {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static final String CLUSTER_NAME = "PeerBox";

    public static void main(String[] args) {
        try (JChannel channel = new JChannel(DefaultProtocols.gossipRouter())) {
            channel.connect(CLUSTER_NAME);
            channel.setReceiver(new LoggingReceiver());

            String hostname = DnsHelper.getHostName();

            int i = 0;
            while(i++ < 10) {
                String text = String.format("Hello from %s!", hostname);
                ObjectMessage message = new ObjectMessage(null, text);
                channel.send(message);
                Sleeper.sleep(2000);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

}
