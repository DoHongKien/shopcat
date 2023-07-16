package com.assignment.user.export;

import com.assignment.Exporter;
import com.assignment.entity.User;
import jakarta.servlet.http.HttpServletResponse;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import java.io.IOException;
import java.util.List;

public class UserCsvExporter extends Exporter {

    public void export(List<User> users, HttpServletResponse response) throws IOException {

        super.responseHeader(response, "text/csv", ".csv", "users_");

        ICsvBeanWriter csvWriter =new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);

        String[] header = {"ID", "Tên", "Ngày sinh", "Số điện thoại", "Địa chỉ", "Email", "Vai trò", "Trạng thái"};
        String[] fieldUser = {"id", "name", "dob", "phone", "address", "email", "roles", "status"};
        csvWriter.writeHeader(header);
        for(User user: users) {
            csvWriter.write(user, fieldUser);
        }
        csvWriter.close();
    }
}
