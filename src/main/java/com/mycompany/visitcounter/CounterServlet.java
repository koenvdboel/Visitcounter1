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
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Visit Counter Servlet
 * Uses AtomicInteger for thread-safe counter increments.
 * Counter is persisted to a file so it survives server restarts.
 */
@WebServlet(name = "CounterServlet", urlPatterns = {"/counter", "/count"})
public class CounterServlet extends HttpServlet {

    private static final String COUNTER_KEY = "visitCount";

    // File saved in the server's home directory so it survives redeployments.
    // You can change this path to any folder your server has write access to.
    private static final String COUNTER_FILE = System.getProperty("user.home") + "/visitcount.txt";

    @Override
    public void init() throws ServletException {
        // On startup, read the saved count from file (or start at 0 if file doesn't exist)
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
            // Called by JavaScript polling — read current value WITHOUT incrementing
            response.setContentType("text/plain");
            response.getWriter().write(String.valueOf(counter.get()));
        } else {
            // Initial page load — increment once, save to file, then forward to JSP
            int currentCount = counter.incrementAndGet();
            saveCountToFile(currentCount);
            request.setAttribute("visitCount", currentCount);
            request.getRequestDispatcher("/index.jsp").forward(request, response);
        }
    }

    /**
     * Reads the saved count from the file.
     * Returns 0 if the file doesn't exist yet.
     */
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

    /**
     * Saves the current count to the file.
     */
    private void saveCountToFile(int count) {
        try {
            Path path = Paths.get(COUNTER_FILE);
            Files.writeString(path, String.valueOf(count));
        } catch (IOException e) {
            log("Could not save counter file. Reason: " + e.getMessage());
        }
    }
}