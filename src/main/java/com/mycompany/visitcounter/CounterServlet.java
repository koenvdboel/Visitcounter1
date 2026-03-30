package com.visitcounter;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@WebServlet(name = "CounterServlet", urlPatterns = {"/counter", "/count"})
public class CounterServlet extends HttpServlet {

    private static final String COUNTER_KEY = "visitCount";
    private static final String COUNTER_FILE = System.getProperty("user.home") + "/visitcount.txt";
    private static final String VISIT_LOG_FILE = System.getProperty("user.home") + "/visitlog.txt";

    @Override
    public void init() throws ServletException {
        int savedCount = readCountFromFile();
        ServletContext context = getServletContext();
        context.setAttribute(COUNTER_KEY, new AtomicInteger(savedCount));
        log("Visit counter initialized at: " + savedCount);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        ServletContext context = getServletContext();
        AtomicInteger counter = (AtomicInteger) context.getAttribute(COUNTER_KEY);

        if (counter == null) {
            counter = new AtomicInteger(readCountFromFile());
            context.setAttribute(COUNTER_KEY, counter);
        }

        String path = request.getServletPath();

        if ("/count".equals(path)) {
            response.setContentType("text/plain");
            response.getWriter().write(String.valueOf(counter.get()));
        } else {
            int currentCount = counter.incrementAndGet();
            saveCountToFile(currentCount);
            appendVisitTimestamp();

            int visitsLast7Days = countVisitsLastDays(7);
            int visitsLast30Days = countVisitsLastDays(30);

            request.setAttribute("visitCount", currentCount);
            request.setAttribute("visitsLast7Days", visitsLast7Days);
            request.setAttribute("visitsLast30Days", visitsLast30Days);

            request.getRequestDispatcher("/index.jsp").forward(request, response);
        }
    }

    private int readCountFromFile() {
        try {
            Path path = Paths.get(COUNTER_FILE);
            if (Files.exists(path)) {
                String content = Files.readString(path).trim();
                return Integer.parseInt(content);
            }
        } catch (IOException | NumberFormatException e) {
            log("Could not read counter file, starting from 0. Reason: " + e.getMessage());
        }
        return 0;
    }

    private void saveCountToFile(int count) {
        try {
            Path path = Paths.get(COUNTER_FILE);
            Files.writeString(path, String.valueOf(count));
        } catch (IOException e) {
            log("Could not save counter file. Reason: " + e.getMessage());
        }
    }

    private void appendVisitTimestamp() {
        try {
            Path path = Paths.get(VISIT_LOG_FILE);
            String timestamp = LocalDateTime.now().toString() + System.lineSeparator();

            Files.writeString(
                    path,
                    timestamp,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND
            );
        } catch (IOException e) {
            log("Could not write visit log. Reason: " + e.getMessage());
        }
    }

    private int countVisitsLastDays(int days) {
        try {
            Path path = Paths.get(VISIT_LOG_FILE);
            if (!Files.exists(path)) {
                return 0;
            }

            List<String> lines = Files.readAllLines(path);
            LocalDateTime cutoff = LocalDateTime.now().minusDays(days);

            int count = 0;
            for (String line : lines) {
                if (line == null || line.trim().isEmpty()) {
                    continue;
                }

                try {
                    LocalDateTime visitTime = LocalDateTime.parse(line.trim());
                    if (visitTime.isAfter(cutoff)) {
                        count++;
                    }
                } catch (Exception e) {
                    log("Skipping invalid visit log line: " + line);
                }
            }

            return count;
        } catch (IOException e) {
            log("Could not read visit log. Reason: " + e.getMessage());
            return 0;
        }
    }
}
