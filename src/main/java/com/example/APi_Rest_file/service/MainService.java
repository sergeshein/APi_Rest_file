package com.example.APi_Rest_file.service;


import com.example.APi_Rest_file.util.RequestException;
import dto.Request;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@Slf4j
public class MainService {

    @Cacheable(value = "max_value", key = "#hashSumFile")
    public Long getMaxValue(Request request, long hashSumFile) {
        log.info("Call getMaxValue method...");
        List<Long> data = getData(request.getFilePath(), hashSumFile);

        long maxValue = data.get(0);

        for (int i = 1; i < data.size(); i++) {
            long current = data.get(i);
            if (current > maxValue) {
                maxValue = current;
            }
        }

        return maxValue;

    }

    @Cacheable(value = "min_value", key = "#hashSumFile")
    public Long getMinValue(Request request, long hashSumFile) {
        log.info("Call getMinValue method...");

        List<Long> data = getData(request.getFilePath(), hashSumFile);

        long minValue = data.get(0);

        for (int i = 1; i < data.size(); i++) {
            long current = data.get(i);
            if (current < minValue) {
                minValue = current;
            }
        }

        return minValue;

    }

    @Cacheable(value = "median", key = "#hashSumFile")
    public Double getMedian(Request request, long hashSumFile) {
        log.info("Call getMedian method...");

        List<Long> data = getData(request.getFilePath(), hashSumFile);

        double median;

        Collections.sort(data);

        int dataSize = data.size();

        if (dataSize % 2 == 0) {
            median = (data.get(dataSize / 2) + data.get((dataSize - 1) / 2)) / 2;
        } else {
            median = data.get( (dataSize - 1) / 2);
        }

        return median;
    }

    @Cacheable(value = "average", key = "#hashSumFile")
    public Double getAverage(Request request, long hashSumFile) {
        log.info("Call getAverage method...");

        List<Long> data = getData(request.getFilePath(), hashSumFile);

        long sum = 0;

        int dataSize = data.size();

        for (Long num : data) {
            sum += num;
        }

        return (double) sum / dataSize;
    }

    @Cacheable(value = "increase_seq", key = "#hashSumFile")
    public List<List<Long>> getIncreaseSeq(Request request, long hashSumFile) {
        log.info("Call getIncreaseSeq method...");

        var data = getData(request.getFilePath(), hashSumFile);

        List<List<Long>> result = new ArrayList<>();

        int increaseSeqCount = 1;
        int maxIncreaseCount = 1;

        var last = data.get(0);

        List<Long> tempData = new ArrayList<>();
        tempData.add(last);

        for (int i = 1; i < data.size(); i++) {
            var current = data.get(i);

            if (last < current)
                increaseSeqCount++;
            else {
                if (increaseSeqCount >= maxIncreaseCount)
                    result.add(new ArrayList<>(tempData));
                increaseSeqCount = 1;
                tempData.clear();
            }

            tempData.add(current);

            if (maxIncreaseCount < increaseSeqCount)
                maxIncreaseCount = increaseSeqCount;

            if (i == data.size() - 1 & increaseSeqCount >= maxIncreaseCount)
                result.add(tempData);

            last = current;
        }

        return removeUnnecessaryElements(result, maxIncreaseCount);
    }
    @Cacheable(value = "decrease_seq", key = "#hashSumFile")
    public List<List<Long>> getDecreaseSeq(Request request, long hashSumFile) {
        log.info("Call getDecreaseSeq method...");

        var data = getData(request.getFilePath(), hashSumFile);

        List<List<Long>> result = new ArrayList<>();

        int decreaseSeqCount = 1;
        int maxDecreaseCount = 1;

        var last = data.get(0);

        List<Long> tempData = new ArrayList<>();
        tempData.add(last);

        for (int i = 1; i < data.size(); i++) {
            var current = data.get(i);

            if (last > current)
                decreaseSeqCount++;
            else {
                if (decreaseSeqCount >= maxDecreaseCount)
                    result.add(new ArrayList<>(tempData));
                decreaseSeqCount = 1;
                tempData.clear();
            }

            tempData.add(current);

            if (maxDecreaseCount < decreaseSeqCount)
                maxDecreaseCount = decreaseSeqCount;


            if (i == data.size() - 1 & decreaseSeqCount >= maxDecreaseCount)
                result.add(tempData);

            last = current;
        }

        return removeUnnecessaryElements(result, maxDecreaseCount);
    }

    private List<List<Long>> removeUnnecessaryElements(List<List<Long>> data, int max) {

        for(int i = 0; i < data.size(); i++) {
            var size = data.get(i).size();
            if (size < max) {
                data.remove(data.get(i));
                i--;
            }
        }

        return data;
    }


    @Cacheable(value = "data", key = "#hashSumFile")
    public List<Long> getData(String filePath, long hashSumFile) {
        List<Long> data = new ArrayList<>();

        try {
            FileReader reader = new FileReader(filePath);

            BufferedReader buff = new BufferedReader(reader);

            String line = buff.readLine();

            while (line != null) {
                long num = Long.parseLong(line);
                data.add(num);

                line = buff.readLine();
            }

        } catch (IOException e) {
            throw new RequestException(e.getMessage());
        }

        return data;
    }

}

