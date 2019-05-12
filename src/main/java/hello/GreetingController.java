package hello;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.EntryType;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.Tracer;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GreetingController {

    private static final String RESOURCE_NAME = "org-fan-sentinel-demo-resource";

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    @RequestMapping("/greeting")
    public Greeting greeting(@RequestParam(value="name", defaultValue="World") String name) {
        Entry entry = null;
        Greeting greeting = null;

        try {
            entry = SphU.entry(RESOURCE_NAME, EntryType.IN);
            greeting = new Greeting(counter.incrementAndGet(),
                    String.format(template, name));

            if (new Random().nextInt(2) < 1) {
                throw new RuntimeException("except");
            }

        } catch (BlockException e) {
            System.out.println("blocked exception");
            greeting = new Greeting(counter.incrementAndGet(),
                    String.format(template, "blocked"));
        } catch (Exception e) {
            Tracer.trace(e);
            System.out.println("runtime exception");
            greeting = new Greeting(counter.incrementAndGet(),
                    String.format(template, "runtime exception"));
        } finally {
            if (entry != null) {
                entry.exit();
            }
        }
        return greeting;
    }
}
