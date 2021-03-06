import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.reduce.HadoopReducer;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

public class HadoopAggregateJob extends Configured implements Tool 
{
 
  @Override
  public int run(String[] args) throws Exception 
  {
    Job job = new Job(getConf());
    job.setJarByClass(getClass());
    job.setJobName(getClass().getSimpleName());
 
    FileInputFormat.addInputPath(job, new Path(args[0]));
    FileOutputFormat.setOutputPath(job, new Path(args[1]));
 
    job.setMapperClass(HadoopMapper.class);
    job.setCombinerClass(HadoopReducer.class);
    job.setReducerClass(HadoopReducer.class);
 
    job.setOutputKeyClass(Text.class);
    job.setOutputValueClass(LongWritable.class);
 
    return job.waitForCompletion(true) ? 0 : 1;
  }
 
  public static void main(String[] args) throws Exception 
  {
    int rc = ToolRunner.run(new HadoopAggregateJob(), args);
    System.exit(rc);
  }
}
