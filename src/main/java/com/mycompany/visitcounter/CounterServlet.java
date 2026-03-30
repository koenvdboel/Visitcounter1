package com.visitcounter;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

@WebServlet(name = "CounterServlet", urlPatterns = {"/counter", "/count"})
public class CounterServlet extends HttpServlet {

    private static final String COUNTER_KEY = "visitCount";
    private static final String VISIT_LOG_KEY = "visitLog";

    @Override
    public void init() throws ServletException {
        ServletContext context = getServletContext();

        if (context.getAttribute(COUNTER_KEY) == null) {
            context.setAttribute(COUNTER_KEY, new AtomicInteger(0));
        }

        if (context.getAttribute(VISIT_LOG_KEY) == null) {
            context.setAttribute(VISIT_LOG_KEY, new CopyOnWriteArrayList<LocalDateTime>());
        }

        log("Visit counter initialized");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        ServletContext context = getServletContext();

        AtomicInteger counter = (AtomicInteger) context.getAttribute(COUNTER_KEY);
        @SuppressWarnings("unchecked")
        List<LocalDateTime> visitLog =
                (List<LocalDateTime>) context.getAttribute(VISIT_LOG_KEY);

        if (counter == null) {
            counter = new AtomicInteger(0);
            context.setAttribute(COUNTER_KEY, counter);
        }

        if (visitLog == null) {
            visitLog = new CopyOnWriteArrayList<>();
            context.setAttribute(VISIT_LOG_KEY, visitLog);
        }

        String path = request.getServletPath();

        if ("/count".equals(path)) {
            response.setContentType("text/plain");
            response.getWriter().write(String.valueOf(counter.get()));
            return;
        }

        int currentCount = counter.incrementAndGet();
        visitLog.add(LocalDateTime.now());

        int visitsLast7Days = countVisitsLastDays(visitLog, 7);
        int visitsLast30Days = countVisitsLastDays(visitLog, 30);

        request.setAttribute("visitCount", currentCount);
        request.setAttribute("visitsLast7Days", visitsLast7Days);
        request.setAttribute("visitsLast30Days", visitsLast30Days);

        request.getRequestDispatcher("/index.jsp").forward(request, response);
    }

    private int countVisitsLastDays(List<LocalDateTime> visitLog, int days) {
        LocalDateTime cutoff = LocalDateTime.now().minusDays(days);
        int count = 0;

        for (LocalDateTime visitTime : visitLog) {
            if (visitTime != null && visitTime.isAfter(cutoff)) {
                count++;
            }
        }

        return count;
    }
}
