package com.github.locis.apps

import org.apache.hadoop.fs.Path
import org.apache.hadoop.io.Text
import org.apache.hadoop.mapreduce.Job
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat

import com.github.locis.map.NeighborSearchMapper
import com.github.locis.reduce.NeighborSearchReducer

object NeighborSearch extends MapReduceJob {

  def jobName: String = {
    "NeighborSearch"
  }

  override protected val errorMsg = "Usage: $HADOOP_HOME/bin/hadoop jar target/uber-locis-0.0.1-SNAPSHOT.jar " +
    "com.github.locis.apps." + jobName + " <input_path_to_read_raw_data> <output_path_to_write_neighbors>"

  def run(inputPath: Path, outputPath: Path, args: Array[String]): Unit = {

    val job = new Job(hadoopConfiguration, jobName)
    job.setMapperClass(classOf[NeighborSearchMapper])
    job.setReducerClass(classOf[NeighborSearchReducer])
    job.setMapOutputKeyClass(classOf[Text])
    job.setMapOutputValueClass((classOf[Text]))
    job.setOutputKeyClass(classOf[Text])
    job.setOutputValueClass(classOf[Text])
    FileInputFormat.addInputPath(job, inputPath)
    FileOutputFormat.setOutputPath(job, outputPath)
    val status = if (job.waitForCompletion(true)) 0 else 1
    System.exit(status)
  }
}