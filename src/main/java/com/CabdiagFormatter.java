
package com;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.interfaces.JavaExecutor;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;


public class CabdiagFormatter implements Runnable {
    private static final Logger LOG = Logger.getLogger(CabdiagFormatter.class);
    private static String[] args;
    private final String daemonName;
    private final JavaExecutor executor;
    private final Class<? extends FormatDaemon> daemonClass;
    private final StringIOConverter converter;


    public static void main(String... args) {
        LOG.debug("Project dir: " + JavaExecutor.PROJECT_DIR);
        CabdiagFormatter.args = args;
        try {
            Injector injector = Guice.createInjector(new DaemonModule());
            CabdiagFormatter instance = injector.getInstance(CabdiagFormatter.class);
            SwingUtilities.invokeLater(instance);
        } catch (Exception e) {
            LOG.error(e);
        }
    }


    public void testRun(String... args) {
        this.args = args;
        run();
    }


    @Inject
    public CabdiagFormatter(JavaExecutor executor,
                            StringIOConverter converter) {
        this(FormatDaemon.class, executor, converter);
    }

    public CabdiagFormatter(Class<? extends FormatDaemon> daemonClass,
                            JavaExecutor executor,
                            StringIOConverter converter) {
        String daemonPackage = daemonClass.getPackage().getName();
        daemonName = daemonClass
                .getName()
                .replace(daemonPackage + '.', "");
        this.executor = executor;
        this.daemonClass = daemonClass;
        this.converter = converter;
    }


    @Override
    public void run() {
        String command;
        boolean hasArgs = !ArrayUtils.isEmpty(args);
        if (hasArgs) {
            command = args[0];
        } else {
            runDaemon();
            return;
        }
        LOG.debug("Run with args: "
                + String.join(" ", args));
        processCommand(command);
    }


    private void processCommand(String command) {
        switch (command) {
            case "stop":
                stopDaemon();
                return;
            case "status":
                checkDaemon();
                return;
            case "help":
            case "-help":
            case "--help":
            case "-h":
            case "?":
            case "/?":
                showHelp();
                return;
            default:
                System.err.println("Unrecognized arg!");
                LOG.error("Unexpected arg: " + command);
                showHelp();
//                throw new IllegalStateException("Unexpected value: " + command);
        }
    }

    private void showHelp() {
        System.out.println(
                "stop    - Остановить.\n" +
                "status    - Узнать, запущен ли.\n" +
                "help      - Справка.\n" +
                "-help     \n" +
                "--help    \n" +
                "-h        \n" +
                "?         \n" +
                "/?        \n"
        );
    }


    private void checkDaemon() {
        if (isDaemonStopped()) {
            System.out.println("Daemon is stopped.");
        } else {
            System.out.println("Daemon is running.");
        }
    }


    private void stopDaemon() {
        if (isDaemonStopped()) {
            System.err.println("Daemon isn't running!");
            return;
        }
        String[] args =
                {"taskkill.exe",
                "/PID",
                String.valueOf(daemonProcessId()),
                "/F"};
        executor.execInConsole(args);
        System.out.println("Daemon was stopped.");
    }


    private void runDaemon() {
        if (!isDaemonStopped()) {
            System.err.println("Daemon's already running!");
            return;
        }

        executor.javaw(daemonClass);
        System.out.println("Daemon was run successfully.");
    }

    private boolean isDaemonStopped() {
        return !processMap().containsValue(daemonName);
    }


    private int daemonProcessId() {
        for (Map.Entry<Integer, String> entry
                : processMap().entrySet()) {
            if (entry.getValue().equals(daemonName)) {
                return entry.getKey();
            }
        }
        return -1;
    }


    private Map<Integer, String> processMap() {
        Map<Integer, String> result = new HashMap<>();
        InputStream psStream = executor
                .jps()
                .getInputStream();
        String jpsOut = converter.read(psStream);
        Scanner sc = new Scanner(jpsOut);

        while (sc.hasNextLine()) {
            String psIdStr = sc.findInLine("[0-9]+");
            String psName = sc.findInLine("\\w+");
            if (psIdStr != null && psName != null) {
                int psId = Integer.parseInt(psIdStr.trim());
                psName = psName.trim();
                result.put(psId, psName);
            }
            if (sc.hasNextLine()) {
                sc.nextLine();
            }
        }
        LOG.debug("Jps process map: " + result);
        return result;
    }

}