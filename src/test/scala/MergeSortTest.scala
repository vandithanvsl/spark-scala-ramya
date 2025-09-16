
// Simple test cases for the MergeSort object
object MergeSortTest {
  def main(args: Array[String]): Unit = {
    assert(MergeSort.mergeSort(List()) == List())
    assert(MergeSort.mergeSort(List(1)) == List(1))
    assert(MergeSort.mergeSort(List(3, 1, 2)) == List(1, 2, 3))
    assert(MergeSort.mergeSort(List(5, 4, 3, 2, 1)) == List(1, 2, 3, 4, 5))
    assert(MergeSort.mergeSort(List(5, 3, 3, 2, 2, 1)) == List(1, 2, 2, 3, 3, 5))
    println("All test cases passed!")
  }
}
